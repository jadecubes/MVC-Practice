package jrails;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Model {
    private int id=0;
    //mid's content can be retrieved and used directly
    static String modelFolder="models";
    static String[] spCharMem={",","\t","\b","\r","\f","\n"," ","\'","\"","\\\\u","\\\\x","\0","\\\\"};
    static String[] spCharDisk={"\\(comma\\)","\\(tab\\)","\\(backspace\\)","\\(return\\)","\\(formfeed\\)","\\(newline\\)","\\(space\\)","\\(squote\\)","\\(dquote\\)","\\(unicode\\)","\\(hexstr\\)","\\(octstr\\)","\\(bslash\\)"};
    private static String serializeString(String str){
        if(str==null) return "(null)";
        if(str.equals("")) return "(empty)";
        for(int i=0;i<spCharMem.length;++i){
            //https://stackoverflow.com/questions/27378449/regex-to-match-all-backslash-characters-except-new-line-carriage-return-etc
            str=str.replaceAll(spCharMem[i],spCharDisk[i]);
        }
        return str;
    }
    private static String deserializeString(String str){
        if(str.equals("(null)")) return null;
        if(str.equals("(empty)")) return "";
        for(int i=0;i<spCharDisk.length;++i){
            str=str.replaceAll(spCharDisk[i],spCharMem[i]);
        }
        return str;
    }
    private static Object closeIO(Object o){
        if(o==null) return null;
        try {
            Method m=o.getClass().getMethod("close",null);
            if(m!=null){
                m.invoke(o,null);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return o;
        }
        return o;
    }
    private static File initModelFile(Class c){
        //write to files
        FileWriter writer=null;
        RandomAccessFile raf=null;
        try {
            File modelFile = new File(modelFolder+"/"+c.getSimpleName()+".db");
            if(modelFile.exists()) return modelFile;

            modelFile.getParentFile().mkdirs();
            modelFile.createNewFile(); // if file already exists will do nothing
            Field[] fields=c.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName,(s1,s2)->{return s1.compareTo(s2);}));
            String fContent="id:int,";
            //header
                for(int i=0;i<fields.length;++i){
                    Field f=fields[i];
                    Annotation ant=f.getAnnotation(Column.class);
                    if(ant==null) continue;
                    if (f.getType()==String.class) {
                        fContent+=f.getName()+":String,";
                    }
                    else if (f.getType()==int.class) {
                        fContent+=f.getName()+":int,";
                    }
                    else if (f.getType()==boolean.class) {
                        fContent+=f.getName()+":boolean,";
                    }
                    else
                        throw new UnsupportedOperationException();
                }
                //do nothing if there are lines in the file
                if( modelFile!=null) {
                    fContent=fContent.substring(0,fContent.length()-1)+"\n";
                    writer = new FileWriter(modelFile, false); // true to append
                    if(writer!=null) {
                        writer.write(fContent);
                        writer= (FileWriter) closeIO((Object) writer);
                    }
                    return modelFile;
                }else {
                    return null;
                }
        } catch (Exception e) {
            e.printStackTrace();
            writer=(FileWriter) closeIO((Object) writer);
            return null;
        }finally {

        }
    }
    private static File getMidFile(){
        File mIdOnDisk = new File(modelFolder+"/id");
        if(mIdOnDisk!=null) {
            mIdOnDisk.getParentFile().mkdir();
            try {
                mIdOnDisk.createNewFile(); // if file already exists will do nothing
                return mIdOnDisk;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else return null;
    }

    private static boolean updateMIdOnDisk(int id) {
        if(id==0) return false;

        FileWriter writer=null;
        try {
            if(getMidFile()!=null) {
                writer = new FileWriter(getMidFile(), false); // true to append / false to overwrite.
                if(writer!=null) {
                    writer.write(Integer.toString(id));
                    writer = (FileWriter) closeIO((Object) writer);
                }
                return true;
            }else return false;
        } catch (IOException e) {
            e.printStackTrace();
            writer=(FileWriter) closeIO((Object) writer);
            return false;
        }finally {

        }
    }
    private static int getMIdOnDisk(){
        BufferedReader reader = null;
        int modelCnt=0;
        //read cnt
        if(getMidFile()!=null) {
            try {
                reader = new BufferedReader(new FileReader(getMidFile()));
                String text = null;
                if (reader !=null ) {
                    if((text = reader.readLine()) != null) {
                        try {
                            modelCnt = Integer.parseInt(text);
                        } catch (Exception e) {
                            //this is regarded as the empty file case
                            reader = (BufferedReader) closeIO((Object) reader);
                            updateMIdOnDisk(Integer.MIN_VALUE);
                            return Integer.MIN_VALUE;
                        }
                    }else{
                        reader = (BufferedReader) closeIO((Object) reader);
                        updateMIdOnDisk(Integer.MIN_VALUE);
                        return Integer.MIN_VALUE;
                    }
                }
                //handle surprises
                reader=(BufferedReader) closeIO((Object) reader);
                if (modelCnt == 0) {
                    updateMIdOnDisk(Integer.MIN_VALUE);
                    return Integer.MIN_VALUE;
                }
                return modelCnt;
            } catch (IOException e) {
                e.printStackTrace();
                reader=(BufferedReader) closeIO((Object) reader);
                return 0;
            }
            finally {

            }
        }else return 0;
    }
    private int getLineOffsetInModelFile(int id){
        File modelFile=initModelFile(getClass());
        RandomAccessFile raf=null;
        int offset=0;
        try {
            // create a new RandomAccessFile with filename test
            raf = new RandomAccessFile(modelFile, "rw");
            String text="";
            while (raf!=null && (text=raf.readLine())!=null){
                List<String > txtList = Arrays.asList(text.split(","));
                if(txtList.get(0).equals(Integer.toString(id))){
                    raf=(RandomAccessFile)closeIO((Object)raf);
                    return offset;
                }
                ++offset;
            }
            raf=(RandomAccessFile)closeIO((Object)raf);
        } catch (IOException ex) {
            ex.printStackTrace();
            raf=(RandomAccessFile)closeIO((Object)raf);
            return -1;
        }
        return -1;
    }
    public void save() {
        File modelFile=initModelFile(getClass());
        //format: id, all fields
        int mid=(id==0)?getMIdOnDisk():id;
        id=(id==0)?mid:id;
        //https://stackoverflow.com/questions/9355343/java-reflection-get-fields-belonging-to-current-class-only
        Field[] fields=getClass().getDeclaredFields();
        String fContent=id+",";
        //sort alphabetically
        Arrays.sort(fields, Comparator.comparing(Field::getName,(s1,s2)->{return s1.compareTo(s2);}));
        FileWriter writer=null;
        try {
            for (int i = 0; i < fields.length; ++i) {
                Field f = fields[i];
                Object value=null;
                Annotation ant = f.getAnnotation(Column.class);
                if(ant==null) continue;
                if (f.getType()==String.class) {
                    value = f.get(this);
                    fContent+=serializeString((String)value)+",";
                }
                else if (f.getType()==int.class) {
                    value = f.getInt(this);
                    fContent+=value+",";
                }
                else if (f.getType()==boolean.class) {
                    value = f.getBoolean(this);
                    fContent+=value+",";
                }
                else
                    throw new UnsupportedOperationException();
            }
            int fOffset=0;
            fContent=fContent.substring(0,fContent.length()-1)+"\n";
            if((fOffset=getLineOffsetInModelFile(id))>0){
                updateModelAtOffset(fOffset,fContent);
            }
            else if(modelFile!=null) {
                writer = new FileWriter(modelFile, true); // true to append
                if(writer!=null) writer.write(fContent);
            }
            writer=(FileWriter) closeIO((Object) writer);
            //mid must >0
            updateMIdOnDisk( (mid+1==0)? (mid+2):(mid+1) );
        }catch (Exception e){
            e.printStackTrace();
            writer=(FileWriter) closeIO((Object) writer);
        }finally {

        }

    }
    private static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
    private void updateModelAtOffset(int ofs,String line){
        File tmp=new File(modelFolder+"/tmp");
        File db=new File(modelFolder+"/"+getClass().getSimpleName()+".db");
        RandomAccessFile source=null;
        FileWriter dest=null;

        int offset=0;
        try {
            if(tmp.exists()) tmp.delete();
            copyFile(db,tmp);
            //clear content
            new PrintWriter(db).close();
            // create a new RandomAccessFile with filename test
            source = new RandomAccessFile(tmp, "rw");
            dest = new FileWriter(db, true); // true to append

            String text="";
            while (source!=null && (text=source.readLine())!=null){
                if(dest!=null) {
                    if(offset==ofs) {
                        if (!line.equals("") && line != null)
                            dest.write(line);
                    }
                    else
                        dest.write(text+"\n");
                }
                ++offset;
            }
            dest= (FileWriter) closeIO((Object) dest);
            source=(RandomAccessFile)closeIO((Object)source);
            if(tmp.exists()) tmp.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
            dest= (FileWriter) closeIO((Object) dest);
            source=(RandomAccessFile)closeIO((Object)source);
        }
    }
    public int id() {
        //compare current object with ones in the model file
        return id;
    }

    public static <T> T find(Class<T> c, int id) {
        File modelFile=initModelFile(c);
        BufferedReader reader = null;
        String text = null;
        boolean is1stLine=true;
        List<String> members = null;
        String className = c.getSimpleName();
        try {
            reader = new BufferedReader(new FileReader(modelFile));
            while (reader!= null && (text = reader.readLine()) != null) {
                if(is1stLine){
                    members = Arrays.asList(text.split(","));
                    is1stLine=false;
                }else{
                    List<String> txtList = Arrays.asList(text.split(","));
                    if(txtList.get(0).equals(Integer.toString(id))){
                        //delete \n
                        //txtList.set(txtList.size() - 1, removeLastChr(txtList.get(txtList.size() - 1)));
                        reader=(BufferedReader) closeIO((Object) reader);
                        return createObj(c,members,txtList);
                    }
                }
            }
            reader=(BufferedReader) closeIO((Object) reader);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            reader=(BufferedReader) closeIO((Object) reader);
            return null;
        }finally {
        }
    }
    private static String removeLastChr(String str){
        return str.substring(0,str.length()-1);
    }
    private static <T> T createObj(Class clazz,List<String> members, List<String> txtList) {
        try {
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();
            //0:id
            for (int x = 0; x < txtList.size(); ++x) {
                String m = members.get(x);
                List<String> d = Arrays.asList(m.split(":"));
                String fName = d.get(0);
                String fType = d.get(1);
                Field f=null;
                if(x==0) {
                    //for id
                    f = clazz.getSuperclass().getDeclaredField(fName);
                    f.setAccessible(true);
                }
                else {
                    f = clazz.getDeclaredField(fName);
                    Annotation ant = f.getAnnotation(Column.class);
                    if (ant == null) continue;
                }
                f.setAccessible(true);

                if (fType.equals("int")) {
                   f.set(object, Integer.parseInt(txtList.get(x)));
                } else if (fType.equals("boolean")) {
                   f.set(object, Boolean.parseBoolean(txtList.get(x)));
                } else if (fType.equals("String")) {
                    f.set(object, deserializeString(txtList.get(x)));
                } else
                    throw new Exception();
            }
            T tObj = (T) clazz.cast(object);
            return tObj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static <T> List<T> all(Class<T> c) {
        // Returns a List<element type>
        //File dir = new File("models");
        File modelFile=initModelFile(c);
        LinkedList<T> list =new LinkedList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(modelFile));
            String text = null;
            boolean isAfter1stLine = false;
            List<String> members = null;

            while (reader!=null && (text = reader.readLine()) != null) {
                List<String> txtList = Arrays.asList(text.split(","));
                if (!isAfter1stLine) {
                    members = Arrays.asList(text.split(","));
                    isAfter1stLine = true;
                } else {
                        list.add(createObj(c,members,txtList));
                }
            }
            reader=(BufferedReader) closeIO((Object) reader);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            reader=(BufferedReader) closeIO((Object) reader);
            return list;
        }finally {

        }
    }

    public void destroy(){
            if (id == 0) throw new RuntimeException();
            else {
                File f = initModelFile(getClass());
                int fOffset = 0;
                if ((fOffset = getLineOffsetInModelFile(id)) > 0) {
                    updateModelAtOffset(fOffset, "");
                } else throw new RuntimeException();
            }
    }

    public static void reset() {
        File dir = new File(modelFolder);
        if(!dir.exists()) return;
        try {
            deleteDirectory(dir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteDirectory(Path directory) throws IOException
    {
        Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

}