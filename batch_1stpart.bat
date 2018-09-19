set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set PSFTP_FILE=D:\app\infa\batch\FTPScripts\PSFTP_sc_MemberReturn.txt
set REMOTE_FILE_LOCATION=/ 
set REMOTE_FILE_NAME=*_SC_MemberWeekly_ReturnFile
set RENAMED_FILE=SC_MemberWeekly_ReturnFile.csv

D:
cd D:\data\ReturnFileWeekly

echo cd %REMOTE_FILE_LOCATION% > %PSFTP_FILE%
echo prompt >> %PSFTP_FILE%
echo mget %REMOTE_FILE_NAME%.zip >> %PSFTP_FILE%

psftp MSRF1RT@sftp.igxfer.com -i D:\app\infa\batch\Infogroup-MSRF1RT-private.ppk -P 22 -b %PSFTP_File% -be >> %loginfo%

if exist %REMOTE_FILE_NAME%.zip (
	 "c:\Program Files\7-Zip\7z.exe" e %REMOTE_FILE_NAME%.zip >> %loginfo%
	  move %REMOTE_FILE_NAME%.zip .\Archive >> %loginfo%
	rename %REMOTE_FILE_NAME% %RENAMED_FILE% >> %loginfo%
) else (
	goto file_not_found
)

:got_file
echo got Files %DATE% %TIME% > %logsuccess%
echo got Files %DATE% %TIME% 
goto done

:file_not_found
echo Could Not Find Files. %REMOTE_FILE_NAME%.zip - %DATE% %TIME% > %logerror%
echo Could not find files.

:done
