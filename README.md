CubeMail - Glydars First SQLite Plugin.
========================================

 Adds Ingame mail to Cubeworld.
 
 Glydar plugin Devs.
 -------------------
 1)Fork and Build just like any other Prodject rembering to add ParaGlydar.jar to your build path.
 
 
 2)
 If you are using Glydar`s source to build your own Glydar.jar, 
 add the following XML fragments into the pom.xml file. With those settings, 
 your Glydar Build will automatically download xerial`s SQLiteJDBC library into your local Maven repository, 
 since xerial`s sqlite-jdbc libraries are synchronized with the Maven's central repository.
 
 
<dependencies>
    <dependency>
      <groupId>org.xerial</groupId>
      <artifactId>sqlite-jdbc</artifactId>
      <version>3.7.2</version>
    </dependency>
</dependencies>
 
3) if your using the Glydar.jar from glydar.org, you will need to down load from https://bitbucket.org/xerial/sqlite-jdbc/downloads and add the folder org in to Glydar.jar.
(it will not over write any Glydar Files).
