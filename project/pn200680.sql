USE [master]
GO
/****** Object:  Database [SabProjekat]    Script Date: 11.7.2023. 06:46:32 ******/
CREATE DATABASE [SabProjekat]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'SabProjekat', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS01\MSSQL\DATA\SabProjekat.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'SabProjekat_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS01\MSSQL\DATA\SabProjekat_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [SabProjekat] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [SabProjekat].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [SabProjekat] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [SabProjekat] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [SabProjekat] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [SabProjekat] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [SabProjekat] SET ARITHABORT OFF 
GO
ALTER DATABASE [SabProjekat] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [SabProjekat] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [SabProjekat] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [SabProjekat] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [SabProjekat] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [SabProjekat] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [SabProjekat] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [SabProjekat] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [SabProjekat] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [SabProjekat] SET  DISABLE_BROKER 
GO
ALTER DATABASE [SabProjekat] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [SabProjekat] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [SabProjekat] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [SabProjekat] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [SabProjekat] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [SabProjekat] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [SabProjekat] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [SabProjekat] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [SabProjekat] SET  MULTI_USER 
GO
ALTER DATABASE [SabProjekat] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [SabProjekat] SET DB_CHAINING OFF 
GO
ALTER DATABASE [SabProjekat] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [SabProjekat] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [SabProjekat] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [SabProjekat] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [SabProjekat] SET QUERY_STORE = ON
GO
ALTER DATABASE [SabProjekat] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [SabProjekat]
GO
/****** Object:  Table [dbo].[Artikal]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Artikal](
	[cijena] [int] NOT NULL,
	[kolicina] [int] NOT NULL,
	[idProdavnice] [int] NOT NULL,
	[naziv] [varchar](100) NOT NULL,
	[idArtikla] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [XPKArtikal] PRIMARY KEY NONCLUSTERED 
(
	[idArtikla] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Grad]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Grad](
	[idGrada] [int] IDENTITY(1,1) NOT NULL,
	[naziv] [varchar](100) NOT NULL,
 CONSTRAINT [XPKGrad] PRIMARY KEY NONCLUSTERED 
(
	[idGrada] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Kupac]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Kupac](
	[idKupca] [int] IDENTITY(1,1) NOT NULL,
	[idGrada] [int] NOT NULL,
	[pare] [decimal](10, 3) NOT NULL,
	[naziv] [varchar](100) NOT NULL,
 CONSTRAINT [XPKKupac] PRIMARY KEY NONCLUSTERED 
(
	[idKupca] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Linija]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Linija](
	[idGrada1] [int] NOT NULL,
	[idGrada2] [int] NOT NULL,
	[daniTransporta] [int] NOT NULL,
 CONSTRAINT [XPKLinija] PRIMARY KEY NONCLUSTERED 
(
	[idGrada1] ASC,
	[idGrada2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Porudzbina]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Porudzbina](
	[idKupca] [int] NOT NULL,
	[idPorudzbine] [int] IDENTITY(1,1) NOT NULL,
	[status] [varchar](100) NOT NULL,
	[daniDoSastavljanja] [int] NULL,
	[dodatniPopust] [int] NOT NULL,
	[datumSlanja] [datetime] NULL,
	[datumPrijema] [datetime] NULL,
 CONSTRAINT [XPKPorudzbina] PRIMARY KEY NONCLUSTERED 
(
	[idPorudzbine] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Pracenje]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Pracenje](
	[idGrada] [int] NOT NULL,
	[idPorudzbine] [int] NOT NULL,
	[preostaloDana] [int] NOT NULL,
	[poredak] [int] NOT NULL,
	[idPracenja] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_Pracenje] PRIMARY KEY CLUSTERED 
(
	[idPracenja] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Prodavnica]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Prodavnica](
	[idProdavnice] [int] IDENTITY(1,1) NOT NULL,
	[popust] [int] NOT NULL,
	[idGrada] [int] NOT NULL,
	[naziv] [varchar](100) NOT NULL,
 CONSTRAINT [XPKProdavnica] PRIMARY KEY NONCLUSTERED 
(
	[idProdavnice] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Selektovani]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Selektovani](
	[kolicina] [int] NULL,
	[idPorudzbine] [int] NOT NULL,
	[idArtikla] [int] NOT NULL,
	[popust] [int] NULL,
	[idSelektovani] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [XPKSelektovani] PRIMARY KEY NONCLUSTERED 
(
	[idSelektovani] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Transakcije]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Transakcije](
	[idProdavnice] [int] NULL,
	[cijena] [decimal](10, 3) NOT NULL,
	[idTransakcije] [int] IDENTITY(1,1) NOT NULL,
	[idKupca] [int] NULL,
	[datumTransakcije] [datetime] NOT NULL,
	[idPorudzbine] [int] NULL,
 CONSTRAINT [XPKTrtansakcije] PRIMARY KEY NONCLUSTERED 
(
	[idTransakcije] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Porudzbina] ADD  CONSTRAINT [default_val_2016087292]  DEFAULT ('created') FOR [status]
GO
ALTER TABLE [dbo].[Porudzbina] ADD  CONSTRAINT [default_popust_1144526053]  DEFAULT ((0)) FOR [dodatniPopust]
GO
ALTER TABLE [dbo].[Artikal]  WITH NOCHECK ADD  CONSTRAINT [fkProdavnicaArtikal] FOREIGN KEY([idProdavnice])
REFERENCES [dbo].[Prodavnica] ([idProdavnice])
GO
ALTER TABLE [dbo].[Artikal] NOCHECK CONSTRAINT [fkProdavnicaArtikal]
GO
ALTER TABLE [dbo].[Kupac]  WITH NOCHECK ADD  CONSTRAINT [fkGradKupac] FOREIGN KEY([idGrada])
REFERENCES [dbo].[Grad] ([idGrada])
GO
ALTER TABLE [dbo].[Kupac] NOCHECK CONSTRAINT [fkGradKupac]
GO
ALTER TABLE [dbo].[Linija]  WITH NOCHECK ADD  CONSTRAINT [fkGradLinija1] FOREIGN KEY([idGrada1])
REFERENCES [dbo].[Grad] ([idGrada])
GO
ALTER TABLE [dbo].[Linija] NOCHECK CONSTRAINT [fkGradLinija1]
GO
ALTER TABLE [dbo].[Linija]  WITH NOCHECK ADD  CONSTRAINT [fkGradLinija2] FOREIGN KEY([idGrada2])
REFERENCES [dbo].[Grad] ([idGrada])
GO
ALTER TABLE [dbo].[Linija] NOCHECK CONSTRAINT [fkGradLinija2]
GO
ALTER TABLE [dbo].[Porudzbina]  WITH NOCHECK ADD  CONSTRAINT [fkKupacPorudzbina] FOREIGN KEY([idKupca])
REFERENCES [dbo].[Kupac] ([idKupca])
GO
ALTER TABLE [dbo].[Porudzbina] NOCHECK CONSTRAINT [fkKupacPorudzbina]
GO
ALTER TABLE [dbo].[Pracenje]  WITH CHECK ADD  CONSTRAINT [FK_Pracenje_Grad] FOREIGN KEY([idGrada])
REFERENCES [dbo].[Grad] ([idGrada])
GO
ALTER TABLE [dbo].[Pracenje] CHECK CONSTRAINT [FK_Pracenje_Grad]
GO
ALTER TABLE [dbo].[Pracenje]  WITH NOCHECK ADD  CONSTRAINT [fkPorudzbinaPracenje] FOREIGN KEY([idPorudzbine])
REFERENCES [dbo].[Porudzbina] ([idPorudzbine])
GO
ALTER TABLE [dbo].[Pracenje] NOCHECK CONSTRAINT [fkPorudzbinaPracenje]
GO
ALTER TABLE [dbo].[Prodavnica]  WITH NOCHECK ADD  CONSTRAINT [fkGradProdavnica] FOREIGN KEY([idGrada])
REFERENCES [dbo].[Grad] ([idGrada])
GO
ALTER TABLE [dbo].[Prodavnica] NOCHECK CONSTRAINT [fkGradProdavnica]
GO
ALTER TABLE [dbo].[Selektovani]  WITH NOCHECK ADD  CONSTRAINT [fkArtikalSelektovani] FOREIGN KEY([idArtikla])
REFERENCES [dbo].[Artikal] ([idArtikla])
GO
ALTER TABLE [dbo].[Selektovani] NOCHECK CONSTRAINT [fkArtikalSelektovani]
GO
ALTER TABLE [dbo].[Selektovani]  WITH NOCHECK ADD  CONSTRAINT [fkPorudzbinaSelektovani] FOREIGN KEY([idPorudzbine])
REFERENCES [dbo].[Porudzbina] ([idPorudzbine])
GO
ALTER TABLE [dbo].[Selektovani] NOCHECK CONSTRAINT [fkPorudzbinaSelektovani]
GO
ALTER TABLE [dbo].[Transakcije]  WITH NOCHECK ADD  CONSTRAINT [fkKupacTransakcije] FOREIGN KEY([idKupca])
REFERENCES [dbo].[Kupac] ([idKupca])
GO
ALTER TABLE [dbo].[Transakcije] NOCHECK CONSTRAINT [fkKupacTransakcije]
GO
ALTER TABLE [dbo].[Transakcije]  WITH NOCHECK ADD  CONSTRAINT [fkPorudzbinaTransakcije] FOREIGN KEY([idPorudzbine])
REFERENCES [dbo].[Porudzbina] ([idPorudzbine])
GO
ALTER TABLE [dbo].[Transakcije] NOCHECK CONSTRAINT [fkPorudzbinaTransakcije]
GO
ALTER TABLE [dbo].[Transakcije]  WITH NOCHECK ADD  CONSTRAINT [fkProdavnicaTransakcije] FOREIGN KEY([idProdavnice])
REFERENCES [dbo].[Prodavnica] ([idProdavnice])
GO
ALTER TABLE [dbo].[Transakcije] NOCHECK CONSTRAINT [fkProdavnicaTransakcije]
GO
ALTER TABLE [dbo].[Porudzbina]  WITH CHECK ADD  CONSTRAINT [dodatan_popust_1278744038] CHECK  (([dodatniPopust]=(0) OR [dodatniPopust]=(2)))
GO
ALTER TABLE [dbo].[Porudzbina] CHECK CONSTRAINT [dodatan_popust_1278744038]
GO
ALTER TABLE [dbo].[Porudzbina]  WITH CHECK ADD  CONSTRAINT [porudzbinaVals] CHECK  (([status]='created' OR [status]='sent' OR [status]='arrived'))
GO
ALTER TABLE [dbo].[Porudzbina] CHECK CONSTRAINT [porudzbinaVals]
GO
/****** Object:  StoredProcedure [dbo].[CalculateFinalPrice]    Script Date: 11.7.2023. 06:46:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[CalculateFinalPrice]
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
GO
USE [master]
GO
ALTER DATABASE [SabProjekat] SET  READ_WRITE 
GO
