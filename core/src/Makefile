.PHONY: all
all: source pdf

.PHONY: clean
clean:
	rm -rf auto
	latexmk -C -f
	rm ReMix.tex
	rm -rf main

.PHONY: directories
directories:
	mkdir -p main/resources
	mkdir -p main/scala/controllers
	mkdir -p main/scala/models
	mkdir -p main/scala/clients

pdf: ReMix.scala-nw
	noweave -delay $< > ReMix.tex
	latexmk -pdf

source: ReMix.scala-nw directories
	notangle -Rmodels/Proof.scala $< > main/scala/models/Proof.scala
	notangle -Rmodels/Goal.scala $< > main/scala/models/Goal.scala
	notangle -Rmodels/Formula.scala $< > main/scala/models/Formula.scala
	notangle -Rmodels/Rule.scala $< > main/scala/models/Rule.scala
	notangle -Rmodels/Plugin.scala $< > main/scala/models/Plugin.scala
	notangle -Rclients/PluginClient.scala $< > main/scala/clients/PluginClient.scala
	notangle -Rmodels/Plugins.scala $< > main/scala/models/Plugins.scala
	notangle -Rmodels/ReMixDatabase.scala $< > main/scala/models/ReMixDatabase.scala
	notangle -Rcontrollers/PluginsController.scala $< > main/scala/controllers/PluginsController.scala
	notangle -Rresources/routes $< > main/resources/routes
	notangle -Rcontrollers/ProofController.scala $< > main/scala/controllers/ProofController.scala
	notangle -Rresources/application.conf $< > main/resources/application.conf
	notangle -Rcontrollers/ProofAction.scala $< > main/scala/controllers/ProofAction.scala
	notangle -Rmodels/Types.scala $< > main/scala/models/Types.scala
	notangle -Rmodels/Errors.scala $< > main/scala/models/Errors.scala

makefile: ReMix.scala-nw
	notangle -RMakefile ReMix.scala-nw | unexpand --first-only > Makefile
