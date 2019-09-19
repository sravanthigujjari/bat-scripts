@echo off
setlocal EnableDelayedExpansion

set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set processfile=D:\data\misc\WebMD_FDB_mapping.csv
rem set renamed=D:\data\misc\WebMD_FDB_mapping_%date:~4%_,2%_%date:~7,2%_%date:~10%.csv
set renamed=WebMD_FDB_mapping_%date:~-4%_%date:~3,2%_%date:~0,2%.csv
set PSFTP_FILE=D:\app\infa\batch\FTPScripts\PSFTP_Medispan_WebMD_FDB.txt
set archive=D:\data\misc\Archive\



if exist %processfile%  (
	echo %processfile% will be renamed >> %loginfo%
	rename %processfile% %renamed% >> %loginfo%
 ) else (
 	goto file_not_found
 )

 if exist =D:\data\misc\%renamed% goto ExecutePSFTP

:ExecutePSFTP

echo Sending files to FTP >> %loginfo%

if exist %PSFTP_FILE% del %PSFTP_FILE%
echo put D:\data\misc\WebMD_FDB_mapping_%date:~4,2%_%date:~7,2%_%date:~10%.csv /WebMD/Mapping File/WebMD_FDB_mapping_%date:~4,2%_%date:~7,2%_%date:~10%.csv >> %PSFTP_FILE%

psftp ftp.singlecare.com -l reportsadmin -pw dgd3@nr3p0rt$ -P 22 -b %PSFTP_FILE% -be >> %loginfo%

goto ArchiveFiles

:ArchiveFiles

move /Y D:\data\misc\%renamed% %archive% >> %loginfo%


goto got_file

:got_file
echo Sent The File %source% %DATE% %TIME% > %logsuccess%
echo Sent The File %DATE% %TIME% 
goto done

:file_not_found
echo Could Not Find File. %source% %DATE% %TIME% > %logerror%
echo Could not find file.
exit 99

:done


