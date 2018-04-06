ReMix Core
==========

The ReMix Core server is where the unifying logic of ReMix occurs. ReMix is based
on a server-client design so that interfaces are as modular as possible. The core
coordinates all the plug-ins, and also provides information about the state of
the proof.

Building
--------

To build the ReMix Core, you will need sbt >= 1.0, noweb, and Docker. The build
process is known to work on macOS 10.13, however Linux is also likely to work.
Because it is based on Docker and the JVM, there is no reason why Windows would
not work, however it is untested.

Before building the core, be sure to edit `remix.cfg` to point towards the plug-
ins that you will be using. They are set up for my machine, and are very unlikely
to work on your setup.

To build the core, running `make` should be sufficient. This will call sbt to
assemble the ReMix jar, and then coordinate building the docker container.

To run the core, call `make run`, however unless the database is online this
will fail. To orchestrate the ReMix boot sequence, I recommend running `run.sh`
from the upper directory.
