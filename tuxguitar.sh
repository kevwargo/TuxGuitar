#!/bin/bash

tgclasspath=$(dirname $(realpath $0))/TuxGuitar/build

exec java -classpath $tgclasspath:/usr/share/swt-3.5/lib/swt.jar -Djava.library.path=/usr/share/tuxguitar/lib/lib:/usr/lib64:/lib:/usr/lib -Xms128m -Xmx128m  -Dtuxguitar.share.path=/usr/share/tuxguitar/lib/share org.herac.tuxguitar.gui.TGMain "$@"
# exec java -classpath $tgclasspath:/usr/share/swt-4.4.2/lib/swt.jar -Djava.library.path=/usr/share/tuxguitar/lib/lib:/usr/lib64:/lib:/usr/lib -Xms384m -Xmx384m  -Dtuxguitar.share.path=/usr/share/tuxguitar/lib/share org.herac.tuxguitar.gui.TGMain "$@"
