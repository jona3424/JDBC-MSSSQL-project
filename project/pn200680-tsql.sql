CREATE PROCEDURE CalculateFinalPrice
    @orderId INT,
    @finalPrice DECIMAL(10, 3) OUTPUT
AS
BEGIN
    DECLARE @totalPrice DECIMAL(10, 3);
    DECLARE @additionalDiscount INT;

    -- Calculate total price of the order
    SELECT @totalPrice = SUM(A.cijena * S.kolicina)
    FROM Selektovani S
    JOIN Artikal A ON S.idArtikla = A.idArtikla
    WHERE S.idPorudzbine = @orderId;

    -- Get additional discount from Porudzbina table
    SELECT @additionalDiscount = dodatniPopust
    FROM Porudzbina
    WHERE idPorudzbine = @orderId;

    -- Calculate final price
    SET @finalPrice = @totalPrice - (@totalPrice * (@additionalDiscount / 100.0));

    -- Output the final price
    SELECT @finalPrice AS FinalPrice;
END;


CREATE TRIGGER UpdateSelektovaniDiscount
ON dbo.Prodavnica
AFTER UPDATE
AS
BEGIN
    IF UPDATE(popust)
    BEGIN
        UPDATE S
        SET S.popust = I.popust
        FROM dbo.Selektovani S
		INNER JOIN dbo.Porudzbina P ON S.idPorudzbine = P.idPorudzbine 
        INNER JOIN dbo.Artikal A ON S.idArtikla = A.idArtikla
        INNER JOIN inserted I ON A.idProdavnice = I.idProdavnice
        WHERE EXISTS (SELECT 1 FROM deleted D WHERE D.idProdavnice = I.idProdavnice) and P.status='created'
    END
END;

CREATE TRIGGER TR_TRANSFER_MONEY_TO_SHOPS ON Porudzbina AFTER UPDATE AS BEGIN
    DECLARE orderCursor CURSOR FOR 
        SELECT idPorudzbine, datumPrijema, dodatniPopust 
        FROM INSERTED
        WHERE status = 'arrived'

    DECLARE @orderId INT
    DECLARE @orderReceivedTime DATETIME
    DECLARE @systemDiscount INT

    OPEN orderCursor
    FETCH NEXT FROM orderCursor INTO @orderId, @orderReceivedTime, @systemDiscount

    WHILE @@FETCH_STATUS = 0 BEGIN
        DECLARE @shopAmountMultiplier DECIMAL(10,3)
		DECLARE @systemAmountMultiplier DECIMAL(10,3)
        IF @systemDiscount = 2
		begin
            SET @shopAmountMultiplier = 0.97
			set @systemAmountMultiplier=0.03
		end
        ELSE
		begin
            SET @shopAmountMultiplier = 0.95
			set @systemAmountMultiplier=0.05
		end
        DECLARE shopCursor CURSOR FOR
            SELECT idProdavnice, SUM(Artikal.cijena*Selektovani.kolicina*(100-Selektovani.popust)/100.0)
            FROM Selektovani
                JOIN Artikal ON (Selektovani.idArtikla = Artikal.idArtikla)
            WHERE idPorudzbine = @orderId
            GROUP BY idProdavnice

        DECLARE @shopId INT
        DECLARE @shopAmount DECIMAL(10,3)
		DECLARE @sysAmount DECIMAL(10,3)
		set @sysAmount=0
        OPEN shopCursor
        FETCH NEXT FROM shopCursor INTO @shopId, @shopAmount

        WHILE @@FETCH_STATUS = 0 BEGIN
			set @sysAmount+=(@shopAmount*@systemAmountMultiplier)
            SET @shopAmount *= @shopAmountMultiplier
			
            INSERT INTO Transakcije (datumTransakcije, idPorudzbine, cijena, idProdavnice) VALUES (@orderReceivedTime, @orderId, @shopAmount, @shopId)

            FETCH NEXT FROM shopCursor INTO @shopId, @shopAmount
        END

		INSERT INTO Transakcije (datumTransakcije, idPorudzbine, cijena) VALUES (@orderReceivedTime, @orderId,@sysAmount)

        CLOSE shopCursor
        DEALLOCATE shopCursor

        FETCH NEXT FROM orderCursor INTO @orderId, @orderReceivedTime, @systemDiscount
    END

    CLOSE orderCursor
    DEALLOCATE orderCursor
END

