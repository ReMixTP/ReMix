#!/usr/bin/env bash

# The ordering chosen here is mostly arbitrary
# with the exception that the core MUST BE LOADED LAST.

for dir in $(ls plugins); do
    # We assume each plug-in has a Makefile we can use to run it.
    # Because they may not as daemons, we push them to the background.
    echo "plugins/$dir"
    cd "plugins/$dir" && make run 
    cd ../..
    sleep 2  # Give them a second to get going
done

# Storage and GUI are both daemons
# We let core run in the foreground
main_components=(storage gui core)
for dir in ${main_components[@]}; do
    cd $dir && make run
    cd ..
done

# Tidy up when done; stop everything!
docker kill $(docker ps -q)
