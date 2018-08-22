REM pick up file from ftp server:
REM 
REM username: informaticauser
REM Password: MS#@8AKqjSD2$t7z for that FTP user
REM server name: ftp.scriptrelief.com 
REM port 22
REM 
REM file name: sc_salesrepsegment.csv
REM 
REM place the above file to this folder:
REM D:\data\B2B2C_Actions
REM 
REM rename: sc_salesrepsegment_current_date
REM 
REM archive:
REM archive renamed file to below folder
REM D:\data\B2B2C_Actions\archive

@echo off

set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set PSFTP_FILE=D:\app\infa\batch\FTPScripts\PSFTP_sc_Export_SalesRepKitFulfillment.txt
set REMOTE_FILE_LOCATION=<location-on-remote-server>
set REMOTE_FILE_NAME=<file-on-remote-server-goes-here-with-complete-file-path>
set RENAMED_FILE=sc_salesrepsegment_%DATE_YYYY%%DATE_MM%%DATE_DD%.csv

echo %REMOTE_FILE_LOCATION% > %PSFTP_FILE%
echo get %REMOTE_FILE_NAME% >> %PSFTP_FILE%
cd D:\data\B2B2C_Actions
psftp ftp.scriptrelief.com -l informaticauser -pw MS#@8AKqjSD2$t7z -P 22 -b %PSFTP_FILE% -be >> %loginfo%

if exist %REMOTE_FILE_NAME% (
    rename %REMOTE_FILE_NAME% %RENAMED_FILE%
    move %RENAMED_FILE% D:\data\B2B2C_Actions\archive
    del %PSFTP_FILE%
) else (
    del %PSFTP_FILE%
    goto file_not_found
)

:got_file
echo Sent The Files %DATE% %TIME% > %logsuccess%
echo Sent The Files %DATE% %TIME% 
goto done

:file_not_found
echo Could Not Find Files. %DATE% %TIME% > %logerror%
echo Could not find files.
REM exit 99

:done
