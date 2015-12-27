#!/bin/bash

tgclasspath=$(dirname $(realpath $0))/TuxGuitar/build
swtjar=/usr/share/swt-3.5/lib/swt.jar
# swtjar=/usr/share/swt-4.4.2/lib/swt.jar

exec java -classpath $tgclasspath:$swtjar -Djava.library.path=/usr/share/tuxguitar/lib/lib:/usr/lib64:/lib:/usr/lib -Xms128m -Xmx128m  -Dtuxguitar.share.path=/usr/share/tuxguitar/lib/share org.herac.tuxguitar.gui.TGMain "$@"
