gDrive
======
Simple java command line application to upload file(s) to Google Drive.

Usage
=====
usage: gdrive [-a <code>] [-d <dir>] [-f <file>] [-h] [-l] -p <props>
 -a <code>    process authorization
 -d <dir>     directory for upload; creates new one if no directory
              exists; default is gdrive-uploads
 -f <file>    upload files
 -h           show this help
 -l           display authorization link
 -p <props>   path to gdrive properties file

How to authorize application
============================
1/ generate authorization URL:
   gdrive -p gdrive.properties -l
2/ copy and paste URL to your browser to receive authorization code
3/ authorize application with received authorization code:
   gdrive -p gdrive.properties -a <code>

How to upload files
===================
Upload file(s) to google drive:
   gdrive -p gdrive.properties -f <file1> <file2> <file3>

Files are uploaded to "gdrive-uploads" directory by default.
If you want to change upload directory:
- change "gdrive.uploadDir" property in properties file
- or pass directory in "-d <dir>" argument:
   gdrive -p gdrive.properties -f <file> -d <dir>

Only direct subdirectory of root directory is supported.