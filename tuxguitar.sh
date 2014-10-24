#!/bin/bash

exec java -classpath $PWD/TuxGuitar/build:/usr/share/swt-3.5/lib/swt.jar -Djava.library.path=/usr/share/tuxguitar/lib/lib:/usr/lib64:/lib:/usr/lib -Xms128m -Xmx128m  -Dtuxguitar.share.path=/usr/share/tuxguitar/lib/share org.herac.tuxguitar.gui.TGMain "$@" 2>/dev/null
