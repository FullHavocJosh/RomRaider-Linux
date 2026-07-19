#!/bin/sh
# Flatpak launcher for RomRaider. A single jar covers both the ECU editor
# and the data logger, selected via the -logger CLI flag (see scripts/run.sh),
# so both desktop actions exec this same wrapper.
set -e

APPDIR=/app/romraider
CP="$APPDIR/RomRaider.jar"
for jar in "$APPDIR"/lib/common/*.jar; do
    CP="$CP:$jar"
done

# ResourceUtil.getBundle() (src/main/java/com/romraider/util/ResourceUtil.java)
# resolves the i18n/ directory relative to the process CWD, not the classpath.
cd "$APPDIR"

exec /app/jre/bin/java \
    -Djava.library.path="$APPDIR/lib/linux/64" \
    -Dawt.useSystemAAFontSettings=lcd \
    -Dswing.aatext=true \
    -Dsun.java2d.d3d=false \
    -Xms64M -Xmx512M \
    -cp "$CP" \
    com.romraider.ECUExec "$@"
