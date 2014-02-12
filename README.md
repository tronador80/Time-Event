Time-Event
==========

## DOPA-VM

To install the Time-Event project you can download the vagrant VM, build it and clone all necessary git projects.

VM offered by the DOPA project:
https://github.com/TU-Berlin/dopa-vm.git

You will find installation details for the VM in the main folder of the project.

To start the VM, run `vagrant up`. You don't need to change the roles of the VM.

## Stratosphere

You need to clone Stratosphere from the following github project:

https://github.com/tronador80/stable-strato.git

Currently, the Time-Event project uses the stabile version of Stratosphere-0.2.1.

## Time-Event

Please, clone the Time-Event project from https://github.com/tronador80/Time-Event.git inside the stable-strato folder.

## Installation

Download the file from https://github.com/w121211/textmining/blob/master/models/wsj-0-18-left3words-distsim.tagger
and copy it to the folder pawel-model/src/main/resources/edu/stanford/nlp/models/pos-tagger/wsj-left3words .

Download the file from http://code.google.com/p/mate-tools/downloads/detail?name=CoNLL2009-ST-English-ALL.anna-3.3.parser.model
and copy it to the folder pawel-model/src/main/resources/models/mate .

Execute Maven with `mvn -DskipTests install` .

a) Put the jars (sopremo-pawel.jar and pawel-model.jar and anna-3.3.jar 
and index-import.jar) in the "lib" directory there where the stratosphere is installed...

b) Also put the nephele-server-0.2.1.jar in "lib" (replace existing). 

## Execution of Meteor queries

1. The paths inside the queries need to be adapted.
2. Start of Jobmanager and SopremoServer:

`start-local.sh` and `start-sopremo-server.sh`

3. Execution of exemplary Meteor script:

`./meteor-client.sh /dopa-vm/time/dopa-samples/on
lyReuters.meteor --configDir ../conf/ --updateTime 1000 --wait`

## For Windows

If you are working e.g. with Eclipse in Windows, you need to adapt the paths in JunitTests:

For further information, see:

https://github.com/stratosphere/bigdataclass.org/issues/1
https://github.com/stratosphere/stratosphere/issues/217
