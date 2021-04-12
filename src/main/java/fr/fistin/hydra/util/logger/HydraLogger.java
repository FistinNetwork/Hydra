package fr.fistin.hydra.util.logger;

import fr.fistin.hydra.Hydra;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class HydraLogger {

    private String loggerName;
    private File file;
    private PrintWriter writer;

    private final Hydra hydra;

    public HydraLogger(Hydra hydra, String loggerName, File file){
        this.hydra = hydra;
        this.loggerName = loggerName.endsWith(" ") ? loggerName : loggerName + " ";
        this.file = file;
    }

    public void createLogFile() {
        if (this.hydra.getConfiguration().isLogFile()) {
            if (this.file != null) {
                try {
                    if (!this.file.exists()){
                        if (this.file.getParentFile() != null) this.file.getParentFile().mkdirs();
                        this.file.createNewFile();
                    }
                    this.writer = new PrintWriter(this.file);
                    Runtime.getRuntime().addShutdownHook(new Thread(this.writer::close));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (this.file.exists()) {
                this.file.delete();
            }
        }
    }

    public void log(LogType type, String message){
        //final String date = String.format("[%s] ", new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(new Date()));
        final String msg = this.loggerName + "[" + type.getName().toUpperCase() + "] " + message;

        if (this.hydra.getConfiguration() != null && this.hydra.getConfiguration().isLogFile()) this.save(msg);
        System.out.println(msg);
    }

    public void printHeaderMessage() {
       this.log(LogType.INFO,"########################################");
       this.log(LogType.INFO,"#####           Starting           #####");
       this.log(LogType.INFO,"#####    Hydra - Fistin Network    #####");
       this.log(LogType.INFO,"##### Authors: Faustin - AstFaster #####");
       this.log(LogType.INFO,"########################################");
    }

    public void printFooterMessage() {
        this.log(LogType.INFO,"########################################");
        this.log(LogType.INFO,"#####           Stopping           #####");
        this.log(LogType.INFO,"#####    Hydra - Fistin Network    #####");
        this.log(LogType.INFO,"##### Authors: Faustin - AstFaster #####");
        this.log(LogType.INFO,"########################################");
    }

    private void save(String message){
        if (this.file != null){
            try {
                if (!this.file.exists()) {
                    this.file.getParentFile().mkdirs();
                    this.file.createNewFile();
                }

                if (this.writer != null) {
                    this.writer.println(message);
                    this.writer.flush();
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

}
