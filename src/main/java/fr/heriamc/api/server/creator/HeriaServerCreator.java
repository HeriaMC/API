package fr.heriamc.api.server.creator;

import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerManager;
import fr.heriamc.api.server.HeriaServerStatus;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.utils.HeriaFileUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;

public class HeriaServerCreator {

    private final HeriaServerManager serverManager;

    private static final Random RANDOM = new Random();

    private final Path mainFile = Paths.get("/home/debian");

    private final Path proxyFile = Paths.get(this.mainFile + "/PROXY");

    private final Path templatesFile = Paths.get(this.mainFile + "/TEMPLATES");
    private final Path basicTemplatesFile = Paths.get(this.templatesFile + "/ALL");

    private final Path serversFile = Paths.get(this.mainFile + "/SERVERS");

    public HeriaServerCreator(HeriaServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public String createServer(HeriaServerType serverType, @Nullable UUID hostId){
        int port = this.nextFreePort();
        String name = serverType.getName() + "-" + this.foundId(serverType);

        this.serverManager.put(new HeriaServer(name,
                serverType,
                HeriaServerStatus.STARTING,
                hostId,
                port,
                System.currentTimeMillis(),
                Collections.emptyList()));

        ExecutorService service = Executors.newFixedThreadPool(5);

        Future<?> scheduledFuture = service.submit(() -> {
            try {
                this.startServer(serverType, port, name, hostId);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        return name;
    }

    private void startServer(HeriaServerType serverType, int port, String name, UUID uuid) throws IOException {
        String folder = this.serversFile + "/" + name;
        String template = this.templatesFile + "/" + serverType.getName().toUpperCase(Locale.ROOT);

        Files.createDirectories(Paths.get(folder));

        HeriaFileUtils.copyDirSafely(folder, this.basicTemplatesFile.toString());
        HeriaFileUtils.copyDirSafely(folder, template);

        HeriaFileUtils.copyFileRecursive(Paths.get(this.proxyFile + "/plugins/HeriaAPI-1.0.0.jar").toFile(),
                Paths.get(folder + "/plugins/HeriaAPI-1.0.0.jar").toFile());

        Path propertiesFile = Paths.get(folder + "/server.properties");
        List<String> lines = Files.readAllLines(propertiesFile);
        lines.set(34, "server-port=" + port);
        Files.write(propertiesFile, lines, StandardOpenOption.TRUNCATE_EXISTING);

        File path = new File(folder + "/server.properties");
        Properties props = new Properties();
        props.load(Files.newInputStream(path.toPath()));
        props.put("motd", name);
        props.store(Files.newOutputStream(path.toPath()), "Minecraft server properties");


        File startFile = new File(folder, "start.sh");

        if(startFile.exists()){
            String s = FileUtils.readFileToString(startFile);
            s = s.replaceAll("%servername%", name);
            FileUtils.writeStringToFile(startFile, s);
        } else {
            String scriptContent = "nice -n 4 screen -dmS " + name + " java -Djava.awt.headless=true -jar -Xms1G -Xmx4G server.jar";
            //if(serverType == HeriaServerType.ONESHOT) scriptContent = "nice -n 4 screen -dmS " + name + " java -Djava.awt.headless=true -javaagent:slimeagent.jar -jar -Xms1G -Xmx4G server.jar";
            Writer output = new BufferedWriter(new FileWriter(folder + "/start.sh"));
            output.write(scriptContent);
            output.close();
        }

        File startScript = new File(folder + "/start.sh");
        if (startScript.exists()) {
            startScript.setExecutable(true);
        }
        File targetDirectory = new File(folder);

        new ProcessBuilder("./start.sh").directory(targetDirectory).start();
    }

    public void deleteServer(String serverName) {
        Runtime runtime = Runtime.getRuntime();

        String folder = this.serversFile + "/" + serverName;

        try {
            Process process = runtime.exec("screen -X -S " + serverName + " kill");
            process.waitFor();
            File file = new File(folder);
            HeriaFileUtils.deleteDir(file);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private int foundId(HeriaServerType serverType) {
        int id = serverType.equals(HeriaServerType.HUB) ? 1 : RANDOM.nextInt(500);
        while (this.serverManager.get(serverType.getName() + "-" + id) != null)
            id++;
        return id;
    }

    private int nextFreePort() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(0);
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serverSocket.getLocalPort();
    }
}
