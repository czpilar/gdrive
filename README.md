gDrive
======

- `gdrive-cmd` is simple java command line application for uploading file(s) to Google Drive.
- `gdrive-core` is core library which provide ability for uploading file(s) to Google Drive.

gDrive as command line application
----------------------------------

### Usage
usage: `gdrive [-a <code>] [-d <dir>] [-f <file>] [-h] [-l] [-p <props>] [-t] [-v]`

 `-a <code>` - process authorization<br/>
 `-d <dir>` - directory for upload; creates new one if no directory exists; default is gdrive-uploads<br/>
 `-f <file>` - upload files<br/>
 `-h` - show this help<br/>
 `-l` - display authorization link<br/>
 `-p <props>` - path to gDrive properties file<br/>
 `-t` - empty trash<br/>
 `-v` -show gDrive version

### How to authorize application manually
1. generate authorization URL:<br/>
   `gdrive -p gdrive.properties -l`
2. copy and paste URL to your browser to receive authorization code
3. authorize application with received authorization code:<br/>
   `gdrive -p gdrive.properties -a <code>`

### How to authorize application automatically
1. generate authorization URL:<br/>
   `gdrive -p gdrive.properties -l -a`
2. copy and paste URL to your browser to receive authorization code
3. application waits 5 minutes to receive authorization code
4. application is authorized automatically with received authorization code

### How to upload files
Upload file(s) to google drive:<br/>
   `gdrive -p gdrive.properties -f <file1> <file2> <file3>`

Files are uploaded to `gdrive-uploads` directory by default.

If you want to change upload directory:

- change `gdrive.uploadDir` property in _properties_ file
- or pass directory in `-d <dir>` argument:<br/>
   `gdrive -p gdrive.properties -f <file> -d <path>/<to>/<dir>`

### How to use properties file
- `gdrive.accessToken` - Google Drive access token; this property is updated automatically by gDrive
- `gdrive.refreshToken` - Google Drive refresh token; this property is updated automatically by gDrive
- `gdrive.uploadDir` - path to dir where files will be uploaded: `<path>/<to>/<dir>`

gDrive as core library
----------------------
gDrive core can be used in any other application to provide ability for uploading file(s) to Google Drive.

### Usage
1. add implementation of `IGDriveCredential` interface to spring context
   or use `SimpleGDriveCredential` or extend `AbstractGDriveCredential`
2. provide client ID as `gdrive.core.drive.clientId` property in spring context
3. provide client secret as `gdrive.core.drive.clientSecret` property in spring context
4. provide redirect URI as `gdrive.core.drive.redirectUri` property in spring context
5. import gDrive spring context with annotation @Import(net.czpilar.gdrive.core.context.GDriveCoreContext.class)
6. autowire `IFileService` and use file uploading methods

License
=======

    Copyright 2014-2025 David Pilar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
