import java.io.File
import scala.io.Source
import scala.xml.XML

object HelloWorld {

  case class GVA(groupId : String, artifactId : String, version : String)

  var aggregatorcount = 0
  var modulecount = 0

  def main(args: Array[String]) {
    
      generateAggregator(new File("c:/work/os/jbosstools/trunk"), 
			 new File("parent-pom.xml"),
			 GVA("org.jboss.tools", "org.jboss.tools.parent.pom", "0.0.1-SNAPSHOT"),
			 GVA("org.jboss.tools", "trunk", "0.0.1-SNAPSHOT")
			 )

    println("Modules: " + modulecount + " Aggregator: " + aggregatorcount)
  }

  def generateModule(dir : File, parentPom : File, parent : GVA, me : GVA) {
    modulecount = modulecount + 1

    var module =
    <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
     <modelVersion>4.0.0</modelVersion> 
      <parent>
       <relativePath>{parentPom.getPath()}</relativePath> 
       <groupId>{parent.groupId}</groupId> 
       <artifactId>{parent.artifactId}</artifactId> 
       <version>{parent.version}</version> 
      </parent>
      <groupId>{me.groupId}</groupId> 
      <artifactId>{me.artifactId}</artifactId> 
      <version>{getVersion(dir)}</version> 
      <packaging>{
	if (dir.getParentFile().getName().equals("tests")) 
	  "eclipse-test-plugin" 
	else if (dir.getParentFile().getName().equals("features")) 
	  "eclipse-feature"
	else
	  "eclipse-plugin"}</packaging> 
    </project>;
    
    val pp = new scala.xml.PrettyPrinter(80,2)


    writePom("Module ", pp.format(module), dir)
    
  }

  def writePom(n : String, pp : String, dir : File) {
    val pomxml = new File(dir, "pom.xml")

    val out = new java.io.FileWriter(pomxml)
    out.write(pp)
    out.close

  }

  def getVersion(dir : File) : String = {
    
    var mf = new File(new File(dir, "META-INF"), "MANIFEST.MF")
    var featurexml = new File(dir, "feature.xml")

    if(mf.exists()) {
    val lines = Source.fromFile(mf).getLines
    for(l <- lines) 
      if(l.contains("Bundle-Version:")) {
	return l.substring("Bundle-Version:".length()).trim()
      }    
    } else if (featurexml.exists()) {
      val data = XML.loadFile(featurexml)
      return (data \ "@version").text
    }
    return dir + " " + featurexml.exists() + " " + mf.exists()
  }

  def getArtifactId(dir : File) : String = {
    
    var mf = new File(new File(dir, "META-INF"), "MANIFEST.MF")
    var featurexml = new File(dir, "feature.xml")

    if(mf.exists()) {
    val lines = Source.fromFile(mf).getLines
    for(l <- lines) 
      if(l.contains("Bundle-SymbolicName:")) {
	val x = l.substring("Bundle-SymbolicName:".length()).trim()
	if(x.indexOf(";")>=0) {
	   return x.substring(0, x.indexOf(";"))
	} else  {
	  return x
	}
      }    
    } else if (featurexml.exists()) {
      val data = XML.loadFile(featurexml)
      return (data \ "@id").text
    }
    return dir + " " + featurexml.exists() + " " + mf.exists()
  }

  def dump(dirs : Collection[File], parentPom : File, parent : GVA, me : GVA) {
    for(f <- dirs) {
      
      val manifest = new File(new File(f, "META-INF"), "MANIFEST.MF")
      val plugins = new File(f, "plugins")
      val tests = new File(f, "tests")
      val features = new File(f, "features")
      val featurexml = new File(f, "feature.xml")

      if(manifest.exists() || featurexml.exists()) { 	
       	  generateModule(f, 
			 new File("../" +  parentPom.getPath()),
			 parent, 
			 GVA(me.groupId, getArtifactId(f), me.version))       
      }      
      
      if(plugins.exists()) {	
	generateAggregator(plugins, 
			   new File("../../" + parentPom.getPath()),
			   parent,
			   GVA(me.groupId, f.getName() , "0.0.1-SNAPSHOT")
			 )
      }
      
      if(tests.exists()) {
	generateAggregator(tests, 
			   new File("../../" + parentPom.getPath()),
			   parent,
			   GVA(me.groupId, f.getName() , "0.0.1-SNAPSHOT")
			 )
      }

      if(features.exists()) {
	generateAggregator(features, 
			   new File("../../" + parentPom.getPath()),
			   parent,
			   GVA(me.groupId, f.getName() , "0.0.1-SNAPSHOT")
			 )
      }

    }
  }

  def generateAggregator(dir : File, 
			 parentPom : File, 
			 parent : GVA,
			 me : GVA
		       ) {
    aggregatorcount = aggregatorcount + 1

    val	 dirs =  dir.listFiles().filter(
      (n) => n.isDirectory() && !n.getName().startsWith(".") 
    )
    
    val realModules = dirs.filter(
      (n) => n.isDirectory() && !n.getName().startsWith(".") && new File(new File(n, "META-INF"), "MANIFEST.MF").exists()
    )
    var modules =
      <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <modelVersion>4.0.0</modelVersion> 
    <parent>
      <relativePath>{parentPom.getPath()}</relativePath> 
      <groupId>{parent.groupId}</groupId> 
      <artifactId>{parent.artifactId}</artifactId> 
      <version>{parent.version}</version> 
    </parent>
      <groupId>{me.groupId}</groupId> 
      <artifactId>{me.artifactId}.{dir.getName()}</artifactId> 
      <version>{me.version}</version> 
    <packaging>pom</packaging> 
      <modules>
    {       
      for(f <- realModules) yield {	    
	<module>{ f.getName() }</module>			
      }	
    }
    </modules>
    </project>;
    
    val pp = new scala.xml.PrettyPrinter(80,2)
    writePom("Aggregator ", pp.format(modules),dir)
    //println(pp.format(modules))

	dump(dirs, parentPom, parent, me)    


  }
  
}


HelloWorld.main(args)
