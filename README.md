gdrive
======
Simple java command line application for uploading file(s) to Google Drive.

Usage
-----
usage: `gdrive [-a <code>] [-d <dir>] [-f <file>] [-h] [-l] -p <props>`

 `-a <code>` - process authorization<br/>
 `-d <dir>` - directory for upload; creates new one if no directory exists; default is gdrive-uploads<br/>
 `-f <file>` - upload files<br/>
 `-h` - show this help<br/>
 `-l` - display authorization link<br/>
 `-p <props>` - path to gdrive properties file

How to authorize application
----------------------------
1. generate authorization URL:<br/>
   `gdrive -p gdrive.properties -l`
2. copy and paste URL to your browser to receive authorization code
3. authorize application with received authorization code:<br/>
   `gdrive -p gdrive.properties -a <code>`

How to upload files
-------------------
Upload file(s) to google drive:<br/>
   `gdrive -p gdrive.properties -f <file1> <file2> <file3>`

Files are uploaded to `gdrive-uploads` directory by default.

If you want to change upload directory:

- change `gdrive.uploadDir` property in _properties_ file
- or pass directory in `-d <dir>` argument:<br/>
   `gdrive -p gdrive.properties -f <file> -d <dir>`

Only direct subdirectory of root directory is supported.
