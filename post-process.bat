Pre Processing commands:

- get file from server (connect to server, get the file from server and put it in required location)
and load to target table 

Post Processing commands::

Run SP
Rename
archive

REM Batch file we wrote----
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
    
sqlcmd -b -S simpleplace.singlecare.com -U mercatus3 -P S1ngl3MeNow -Q  "exec singlecare.dbo.a_ETL_SP_SalesRepSegment"
REM sqlcmd.exe -S Simpleplace.SingleCare.com -U mercatus3 -P S1ngl3MeNow -d SingleCare /Q	
	IF ERRORLEVEL 1 (
		echo Error executing a_ETL_SP_SalesRepSegment >> %loginfo%   
	) ELSE (
		echo Success a_ETL_SP_SalesRepSegment >> %loginfo%
	)
	
	rename %REMOTE_FILE_NAME% %RENAMED_FILE%
    move %RENAMED_FILE% D:\data\B2B2C_Actions\archive
    REM del %PSFTP_FILE%

) else (
    REM del %PSFTP_FILE%
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
