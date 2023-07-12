CREATE TRIGGER TR_TRANSFER_MONEY_TO_SHOPS
ON Porudzbina
AFTER UPDATE
AS
BEGIN
    -- Check if the status of the updated row is 'arrived'
    IF UPDATE(status) AND (SELECT status FROM inserted) = 'arrived'
    BEGIN
        DECLARE @orderId INT;
        DECLARE @orderReceivedTime DATETIME;
        DECLARE @systemDiscount BIT;
        
        -- Create a cursor for the updated orders
        DECLARE orderCursor CURSOR FOR
        SELECT idPorudzbine, datumPrijema, dodatniPopust
        FROM inserted
        WHERE status = 'arrived';
        
        -- Open the cursor
        OPEN orderCursor;
        
        -- Fetch the first row
        FETCH NEXT FROM orderCursor INTO @orderId, @orderReceivedTime, @systemDiscount;
        
        WHILE @@FETCH_STATUS = 0
        BEGIN
            DECLARE @buyerMoney DECIMAL(10, 3);
            DECLARE @shopAmountMultiplier DECIMAL(10, 3);
            DECLARE @systemAmountMultiplier DECIMAL(10, 3);
            
            -- Get the buyer's money for the order
            SELECT @buyerMoney = cijena FROM Transakcije WHERE idPorudzbine = @orderId;
            
            -- Determine the shop and system amount multipliers based on the additional discount
            IF @systemDiscount = 1
            BEGIN
                SET @shopAmountMultiplier = 0.97;
                SET @systemAmountMultiplier = 0.03;
            END
            ELSE
            BEGIN
                SET @shopAmountMultiplier = 0.95;
                SET @systemAmountMultiplier = 0.05;
            END
            
            -- Create a cursor for the shops in the order
            DECLARE shopCursor CURSOR FOR
            SELECT idProdavnice, SUM(Artikal.cijena * Selektovani.kolicina * (1 - Selektovani.popust / 100)) * @shopAmountMultiplier AS shopAmount
            FROM Selektovani
            JOIN Artikal ON Selektovani.idArtikla = Artikal.idArtikla
            WHERE idPorudzbine = @orderId
            GROUP BY idProdavnice;
            
            -- Declare variables for shop data
            DECLARE @shopId INT;
            DECLARE @shopAmount DECIMAL(10, 3);
            
            -- Open the shop cursor
            OPEN shopCursor;
            
            -- Fetch the first row
            FETCH NEXT FROM shopCursor INTO @shopId, @shopAmount;
            
            WHILE @@FETCH_STATUS = 0
            BEGIN
                -- Calculate the shop's share of the money
                DECLARE @shopMoney DECIMAL(10, 3);
                SET @shopMoney = @buyerMoney * @shopAmount / (SELECT SUM(Artikal.cijena * Selektovani.kolicina * (1 - Selektovani.popust / 100)) FROM Selektovani WHERE idPorudzbine = @orderId);
                
                -- Insert transaction for each shop in the order
                INSERT INTO Transakcije (idProdavnice, cijena, idKupca, datumTransakcije, idPorudzbine)
                SELECT @shopId, @shopMoney, NULL, @orderReceivedTime, @orderId;
                
                -- Fetch the next row
                FETCH NEXT FROM shopCursor INTO @shopId, @shopAmount;
            END;
            
            -- Close and deallocate the shop cursor
            CLOSE shopCursor;
            DEALLOCATE shopCursor;
            
            -- Calculate the system's share of the money
            DECLARE @systemMoney DECIMAL(10, 3);
            SET @systemMoney = @buyerMoney * @systemAmountMultiplier;
            
            -- Insert system transaction
            INSERT INTO Transakcije (idProdavnice, cijena, idKupca, datumTransakcije, idPorudzbine)
            SELECT NULL, @systemMoney, NULL, @orderReceivedTime, @orderId;
            
            -- Fetch the next row
            FETCH NEXT FROM orderCursor INTO @orderId, @orderReceivedTime, @systemDiscount;
        END;
        
        -- Close and deallocate the order cursor
        CLOSE orderCursor;
        DEALLOCATE orderCursor;
    END;
END;
