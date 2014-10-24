#!/bin/bash

ant -Dnoget=true -Dmaven.mode.offline=true -Dbuild.sysclasspath=ignore -Dmaven.test.skip=true build
