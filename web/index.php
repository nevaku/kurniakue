

<!DOCTYPE html>
<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="sync.js" ></script>
        <script src="kurnia.js" ></script>
    </head>
    <body>
        <?
        $Title = "Kurnia Kue";
        $servername = "localhost";
        $username = "kurz1875_ngadimin";
        $password = "penguasaKue0-";
        ?>
        
        <?php
        echo "Selamat datang di " . $Title . "<BR/>";
        try {
            $conn = new PDO("mysql:host=$servername;dbname=kurz1875_kurniakuedb", $username, $password, array(PDO::ATTR_PERSISTENT => true));
            // set the PDO error mode to exception
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            echo "Silakan"; 
            }
        catch(PDOException $e)
            {
            echo "Connection failed: " . $e->getMessage();
            }
        
        $conn = null;
        ?>
    </body>
</html>