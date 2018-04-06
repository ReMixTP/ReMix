ReMix
=====

ReMix is a Heterogeneous Reasoning (HR) framework and toolbox. Prove theorems with
predicate logic, spider diagrams, or many other reasoning systems all in one proof.
ReMix is designed for modern software development and use, notably to be deployed
online, and build from a collection of freestanding services. ReMix is based on
[MixR](http://github.com/urbas/mixr), another HR framework.



Build and Deploy ReMix
----------------------

ReMix is not entirely trivial to build and deploy. While the majority of the work
can be done through first building `core`, `gui`, and `storage`, there are a few
prerequisites that must be met.

Building ReMix requires:
 - noweb
 - sbt >= 1.0.0
 - Docker

If these dependencies are met, first build `core`; it is the most complicated
component of the three by far. Instructions are found within the
[README](core/README.md) file for core. We will have to rebuild `core` later,
but it is good to verify it works now rather before we make changes.

Next build `storage`. This is done via `make`, and should rarely be needed more
than once. If the database schema changes, then a rebuild will be required.

Finally, build `gui`. This should be trivially achieved with `make`.

The next step is to install plug-ins. This is a three-step process:
 1. In a subdirectory called `plugins`, clone the plug-in repo.
 2. Build the repo according to instructions.
 3. Add the access URL to [core/remix.cfg]. See plug-in documentation for details.

At this point, we must remake the core docker container, so run `make docker` in
the `core` directory. This should be much quicker than the first build. At this
point, run `./run.sh` from the top-level ReMix directory,
and point your web browser to [localhost:8000](http://localhost:8000).



Extending ReMix
---------------

To be determined...
