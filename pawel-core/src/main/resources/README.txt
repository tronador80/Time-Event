- when using Sopremo operators from this project, dima-uima, 
	DimaTextminingCore projects (from timeline-project) and 
	all dependencies that DimaTextmingCore requires, should be on classpath
(dima-uima can be found in /lib directory, DimaTextminingCore is to large >400MB)
	* org.json
	* edu.stanford.nlp.stanford-postagger
	* edu.stanford.nlp.stanford-parser
	* se.lth.cs.mate-tools.anna
	* uimaj-core
	* ... (which dependencies are required can be also found in pom->dependencies)
- building (with tests) memory should be increased


- project is not ready (FOR EXAMPLE: time operator should be able to take arguments like
	narratives/news, heideltime/sutime; until now tests are only very primitive; 
	summary generation operator not ready)
- complete installation & usage manual SOON 
- ...


- ./meteor-client.sh -f /home/pawel/Development/meteor_scripts/test_pawel.script
