set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set PSFTP_FILE=D:\app\infa\batch\FTPScripts\PSFTP_sc_salesrepsegment.txt
set REMOTE_FILE_LOCATION=/PartnerData/SingleCare  
set REMOTE_FILE_NAME=sc_salesrepsegment.csv
set RENAMED_FILE=sc_salesrepsegment_%DATE_YYYY%%DATE_MM%%DATE_DD%.csv

echo cd %REMOTE_FILE_LOCATION% > %PSFTP_FILE%
echo get %REMOTE_FILE_NAME% >> %PSFTP_FILE%
cd D:\data\B2B2C_Actions

psftp ftp.scriptrelief.com -l informaticauser -pw MS#@8AKqjSD2$t7z -P 22 -b %PSFTP_FILE% -be >> %loginfo%

if exist %REMOTE_FILE_NAME% (
    goto got_file
) else (
    goto file_not_found
)

:got_file
echo got Files %DATE% %TIME% > %logsuccess%
echo got Files %DATE% %TIME% 
goto done

:file_not_found
echo Could Not Find Files. %DATE% %TIME% > %logerror%
echo Could not find files.

:done
