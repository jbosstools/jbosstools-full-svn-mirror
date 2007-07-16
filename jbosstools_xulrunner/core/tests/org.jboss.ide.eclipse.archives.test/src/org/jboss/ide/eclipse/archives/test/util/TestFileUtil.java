package org.jboss.ide.eclipse.archives.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class TestFileUtil {
	
	/**
	 * Delete it all
	 * @param dir
	 */
	public static void clean(File file) {
		File[] kids = file.listFiles();
		for( int i = 0; i < kids.length; i++ ) 
			clean(kids[i]);
		file.delete();
	}
	
    public static void copyDirectory (File srcDir, File destDir, boolean recurse) throws IOException
    {
        if (srcDir.isDirectory())
        {
            File[] files = srcDir.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                File copyDest = new File(destDir, files[i].getName());
                if (files[i].isDirectory())
                {
                    copyDest.mkdirs();
                    
                    if (recurse)
                        copyDirectory(files[i], copyDest, true);
                }
                else {
                    copy (files[i], copyDest);
                }
            }
        }
    }
    
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    public static boolean projectFileExists (IProject project, String path)
    {
       return projectFileExists (project, new Path(path));
    }
    
    public static boolean projectFileExists (IProject project, IPath path)
    {
       IFile file = project.getFile(path);
       return file.exists();
    }

}
