package edu.dlf.refactoring.copy.jar;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.dlf.refactoring.copy.Design.IBinaryClass;
import edu.dlf.refactoring.copy.Design.IMergable;
import edu.dlf.refactoring.copy.Design.ISearchQuery;
import edu.dlf.refactoring.copy.Design.ISearchResult;
import edu.dlf.refactoring.copy.Design.ISearchable;
import edu.dlf.refactoring.copy.FileAnalyzer;

public class BinaryClassesRepository extends FileAnalyzer implements ISearchable {

	private final Logger logger;
	private final String jarDir;
	private final Stream<IBinaryClass> allClasses;

	@Inject
	public BinaryClassesRepository(
			Logger logger, 
			@Named("jar") String jarDir) {
		super(logger);
		this.logger = logger;
		this.jarDir = jarDir;
		this.allClasses = getBinaryClassesInDirectory(this.jarDir);
	}

	private Stream<IBinaryClass> getBinaryClassesInDirectory(String directory) {
		return this.getAllFilePaths(directory).filter(p -> p.toString().
			toLowerCase().endsWith(".jar")).flatMap(this::readJarFile);
	}
	
	private Stream<IBinaryClass> readJarFile(Path path) {
		try{
			List<IBinaryClass> allClasses = new ArrayList<IBinaryClass>();
			JarFile file = new JarFile(path.toFile());
			Enumeration<JarEntry> entries = file.entries();
		    while (entries.hasMoreElements()) {
		        JarEntry entry = entries.nextElement();
		        String entryName = entry.getName();
		        if (entryName.endsWith(".class")) {
		            ClassNode classNode = new ClassNode();
		            InputStream classFileInputStream = file.getInputStream(entry);
			            try {
			                ClassReader classReader = new ClassReader(
			                	classFileInputStream);
			                classReader.accept(classNode, 0);
			            } finally {
			                classFileInputStream.close();
			            }
			            allClasses.add(createBinaryClass(classNode));
			        }
			 }
		    file.close();
		    return mergeClassesWithSameName(allClasses.stream());
		}catch(Exception e) {
			logger.fatal(e);
			return Stream.empty();
		}
	}
	
	private IBinaryClass createBinaryClass(ClassNode classNode) {
		AbstractClassVisitor visitor = new ClassStructureVisitor();
		classNode.accept(visitor);
		return (IBinaryClass)visitor.getVisitResult();
	}
	
	private Stream<IBinaryClass> mergeClassesWithSameName(Stream<IBinaryClass> 
		allClasses) {
		Map<String, List<IBinaryClass>> groups = allClasses.collect(Collectors.
			groupingBy(IBinaryClass::getName));
		return groups.values().stream().map(l -> (IBinaryClass)(l.stream().
			collect(Collectors.reducing(IMergable::merge)).get()));
	}

	@Override
	public Stream<ISearchResult> search(ISearchQuery query) {
		return allClasses.flatMap(c -> c.search(query));
	}
}
