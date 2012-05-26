import java.io.File
import scala.io.Source
import scala.xml.XML

object GenPom {

  case class GVA(groupId : String, artifactId : String, version : String)

  /********** Configuration Start **********/
  var projectName = "org.jboss.tools"
  var pathToParentPom = ""
  var parentPomVersion = "0.0.1-SNAPSHOT"
  var sourcePomVersion = "0.0.1-SNAPSHOT"
  /********** Configuration Ends  **********/

  var aggregatorcount = 0
  var modulecount = 0
  
  def main(args: Array[String]) {
    
      generateAggregator(new File("."), 
			 new File(pathToParentPom + "parent-pom.xml"),
			 GVA(projectName, projectName + ".parent.pom", parentPomVersion),
			 GVA(projectName, "trunk", sourcePomVersion)
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
      <packaging>{ if (dir.getParentFile().getAbsolutePath().endsWith("/tests") || dir.getParentFile().getAbsolutePath().endsWith("/tests/.")) 
	  "eclipse-test-plugin" 
	else if (dir.getParentFile().getAbsolutePath().endsWith("/features") || dir.getParentFile().getAbsolutePath().endsWith("/features/.")) 
	  "eclipse-feature"
	else
	  "eclipse-plugin"}</packaging>
	  	{ getTarget(dir) }
     </project>;
		
    val pp = new scala.xml.PrettyPrinter(80,2)

    writePom("Module ", pp.format(module), dir)
    
  }

  def getTarget(dir : File) : Object ={
  	var env = <environment/>;
	
	 if (dir.getAbsolutePath().endsWith("gtk.linux.x86")) {
		env = <environment>
		<os>linux</os>
		<ws>gtk</ws>
		<arch>x86</arch>
		</environment>;
	} else if (dir.getAbsolutePath().endsWith("gtk.linux.x86_64")) {
		env = <environment>
		<os>linux</os>
		<ws>gtk</ws>
		<arch>x86_64</arch>
		</environment>;
	} else if (dir.getAbsolutePath().endsWith("carbon.macosx")) {
		env = <environment>
		<os>macosx</os>
		<ws>carbon</ws>
		<arch>x86</arch>
		</environment>;
	} else if (dir.getAbsolutePath().endsWith("cocoa.macosx")) {
		env = <environment>
		<os>macosx</os>
		<ws>cocoa</ws>
		<arch>x86</arch>
		</environment>;
	} else if (dir.getAbsolutePath().endsWith("win32.win32.x86")) {
		env = <environment>
		<os>win32</os>
		<ws>win32</ws>
		<arch>x86</arch>
		</environment>;
	}
	
	var target = <build>
			<plugins>
			<plugin>
				<groupId>org.sonatype.tycho</groupId>
				<artifactId>target-platform-configuration</artifactId>
				<version>${{tychoVersion}}</version>
				<configuration>
					<resolver>p2</resolver>
					<environments>
						{ env }
					</environments>
				</configuration>
			</plugin>
		</plugins>
		</build>;
		
  	if(dir.getAbsolutePath().endsWith("x86_64") 
  		|| dir.getAbsolutePath().endsWith("x86")
  		|| dir.getAbsolutePath().endsWith("macosx")) {
  		return target
  	}
	return ""
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
	return l.substring("Bundle-Version:".length()).trim().replaceAll("\\.qualifier","-SNAPSHOT")
      }    
    } else if (featurexml.exists()) {
      val data = XML.loadFile(featurexml)
      return (data \ "@version").text.replaceAll("\\.qualifier","-SNAPSHOT")
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
      var aggregate = false  
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
    	  aggregate = true
    	  generateAggregator(plugins, 
			   new File("../../" + parentPom.getPath()),
			   parent,
			   GVA(me.groupId, f.getName() + ".plugins" , "0.0.1-SNAPSHOT")
			 )
      }
      
      if(tests.exists()) {
        aggregate = true
        generateAggregator(tests, 
			   new File("../../" + parentPom.getPath()),
			   parent,
			   GVA(me.groupId, f.getName() + ".tests", "0.0.1-SNAPSHOT")
			 )
      }

      if(features.exists()) {
    	  aggregate = true
    	  generateAggregator(features, 
			   new File("../../" + parentPom.getPath()),
			   parent,
			   GVA(me.groupId, f.getName()+".features" , "0.0.1-SNAPSHOT")
			 )
      }

      if(aggregate) {
    	  println("Generate Agg for " + f)
    	  generateAggregator(f, new File("../" + parentPom.getPath()), parent, GVA(me.groupId, f.getName()+".all", "0.0.1-SNAPSHOT"))
      }
    }
  }

  def isModule(n : File) : Boolean = {
    def v = (new File(n, "pom.xml").exists() && !n.getName().equals("docs")) ||
      (!n.getName().contains(".sdk.") && (new File(new File(n, "META-INF"), "MANIFEST.MF").exists()) || (new File(n, "feature.xml").exists())) || (hasDirectory(n, "features") || hasDirectory(n, "tests")	|| hasDirectory(n, "plugins"))
    return v
  }
  
  def hasDirectory(parent : File, name : String) : Boolean = {
   
    val dir = new File(parent, name)
    return dir.isDirectory() && dir.exists()
  }
  
  def generateAggregator(dir : File, 
			 parentPom : File, 
			 parent : GVA,
			 me : GVA
		       ) {
    aggregatorcount = aggregatorcount + 1

    val	 dirs =  dir.listFiles().filter(
      (n) => n.isDirectory() && !n.getName().startsWith(".") && !n.getName().contains(".sdk.")
    )
    
    val realModules = dirs.filter(
    		(n) => isModule(n))
    
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
      <artifactId>{me.artifactId}</artifactId> 
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


GenPom.main(args)
