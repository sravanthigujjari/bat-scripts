@echo off
setlocal EnableDelayedExpansion

set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set processfile=D:\data\outbound\FetchExport\SC_Fetch_Export_Out.csv
set RENAMED_FILE=SC_Fetch_Export_Out_%DATE_YYYY%%DATE_MM%%DATE_DD%.csv
set archive=D:\data\outbound\FetchExport\Archive
set PSFTP_FILE=D:\data\FTPScripts\PSFTP_sc_fetch_test.txt
set S3_bucket_filepath=s3://rxsense-partner-data-qa/fetch/temp_archive/
set path=%path%;c:\Program Files\7-Zip


if exist %PSFTP_FILE% del %PSFTP_FILE%

if exist %processfile% (
  ren %processfile% %RENAMED_FILE%
  echo Sending files to S3 bucket. >> %loginfo%
  aws s3 cp %RENAMED_FILE% %S3_bucket_filepath% --profile Fetch >> %loginfo%
  7z.exe a "D:\data\outbound\FetchExport\SC_Fetch_Export_Out_%DATE_YYYY%%DATE_MM%%DATE_DD%.zip" "D:\data\outbound\FetchExport\%RENAMED_FILE%" >> %loginfo%
  febootimail -config "D:\app\infa\batch\febooti_config_sc_Fetch_test.txt" >> %loginfo%
  move /Y D:\data\outbound\FetchExport\SC_Fetch_Export_Out_*.zip %archive% >> %loginfo%
  goto got_file
) else (
    goto file_not_found
)

:got_file
echo Sent The File %source% %DATE% %TIME% > %logsuccess%
echo Sent The File %DATE% %TIME% 
goto done

:file_not_found
echo Could Not Find File. %source% %DATE% %TIME% > %logerror%
echo Could not find file.
exit 99

:done

