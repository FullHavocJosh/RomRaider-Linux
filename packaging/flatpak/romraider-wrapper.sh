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

# Works around a Swing/AWT bug where table and other content panes render
# solid black under some XWayland compositors (observed on KDE/KWin) unless
# this is set. It has to be a real process environment variable -- AWT's
# native X11 code reads it via getenv(), so it can't be passed as a JVM
# -D system property alongside the -D flags below.
export _JAVA_AWT_WM_NONREPARENTING=1

exec /app/jre/bin/java \
    -Djava.library.path="$APPDIR/lib/linux/64" \
    -Dawt.useSystemAAFontSettings=lcd \
    -Dswing.aatext=true \
    -Dsun.java2d.d3d=false \
    -Xms64M -Xmx512M \
    -cp "$CP" \
    com.romraider.ECUExec "$@"
