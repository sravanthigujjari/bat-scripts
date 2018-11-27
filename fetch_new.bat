@echo off
setlocal EnableDelayedExpansion

set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set archive=D:\data\outbound\FetchExport\Archive
set path=%path%;c:\Program Files\7-Zip
set processfile=D:\data\outbound\FetchExport\SC_Fetch_Export_Out.csv
set renamed=SC_Fetch_Export_Out_%DATE_YYYY%%DATE_MM%%DATE_DD%.csv
set S3_bucket_filepath=s3://rxsense-partner-data-qa/fetch/temp_archive/

if exist %processfile%  (
	echo %processfile% will be renamed >> %loginfo%
	rename %processfile% %renamed% >> %loginfo%
) else (
	goto file_not_found
)

if exist =D:\data\outbound\FetchExport\%renamed% goto ExecuteUploadS3

:ExecuteUploadS3

echo Sending files to s3 >> %loginfo%

if exist %renamed% 

echo Sending files to S3 bucket. >> %loginfo%

aws s3 cp %renamed% %S3_bucket_filepath% --profile Fetch >> %loginfo%

goto ZipEmailFile

:ZipEmailFile

7z.exe a "D:\data\outbound\FetchExport\SC_Fetch_Export_Out_%DATE_YYYY%%DATE_MM%%DATE_DD%.zip" "D:\data\outbound\FetchExport\%renamed%"

febootimail -config "D:\app\infa\batch\febooti_config_sc_Fetch_test.txt"


goto ArchiveFiles

:ArchiveFiles

move /Y D:\data\outbound\FetchExport\SC_Fetch_Export_Out_*.zip %archive% >> %loginfo%
move /Y D:\data\outbound\FetchExport\%renamed% %archive% >> %loginfo%


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
