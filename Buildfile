repositories.remote << 'http://repository.cloudera.com/artifactory/cloudera-repos/'
repositories.remote << 'http://repo1.maven.org/maven2'

FLUME = transitive(group('flume-ng-core','flume-ng-sdk', :under=>'org.apache.flume', :version=>'1.3.0-cdh4.2.0'))
JUNIT = 'junit:junit:jar:4.10'

desc "Flume Extensions"
define "flume-ng-ext" do
  eclipse.natures :java  
  
  project.version = "0.6"
  compile.options.target = '1.6'
  deps = FLUME

  compile.with deps
  test.with JUNIT

  package :jar
end

