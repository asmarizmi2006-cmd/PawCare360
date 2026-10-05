#!/bin/sh
# Build jar helper
set -e
cd "$(dirname "$0")"
rm -rf /tmp/pc360 dist && mkdir -p /tmp/pc360 dist/lib
javac --release 21 -cp "lib/*" -d /tmp/pc360 -sourcepath src $(find src -name "*.java")
(cd src && find . -type f ! -name "*.java" ! -name "*.form" -exec cp --parents {} /tmp/pc360/ \;)
python3 - <<'PY'
import os
cp='Class-Path: '+' '.join('lib/'+j for j in sorted(os.listdir('lib')))
out=[];line=cp
while len(line.encode())>70:
    out.append(line[:70]); line=' '+line[70:]
out.append(line)
open('/tmp/pc360-manifest.txt','w',newline='').write('Manifest-Version: 1.0\r\nMain-Class: view.LoginForm\r\n'+'\r\n'.join(out)+'\r\n')
PY
jar cfm dist/PawCare360.jar /tmp/pc360-manifest.txt -C /tmp/pc360 .
cp lib/*.jar dist/lib/
