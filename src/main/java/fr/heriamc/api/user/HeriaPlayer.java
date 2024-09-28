package fr.heriamc.api.user;

import fr.heriamc.api.data.NonPersistantData;
import fr.heriamc.api.data.SerializableData;
import fr.heriamc.api.user.nick.NickPlayerData;
import fr.heriamc.api.user.rank.HeriaRank;

import java.util.List;
import java.util.UUID;

public class HeriaPlayer implements SerializableData<UUID> {

    private UUID id;
    private String name;
    private HeriaRank rank;

    private long firstConnection;

    @NonPersistantData
    private String clientBrand;

    @NonPersistantData
    private String connectedTo;

    @NonPersistantData
    private UUID reply;

    @NonPersistantData
    public boolean removedTag;

    @NonPersistantData
    public boolean mod;

    @NonPersistantData
    public NickPlayerData nickData;

    private List<UUID> friends;
    private List<UUID> pendingFriendsRequests;
    private List<UUID> sentFriendsRequests;

    private float coins;
    private int hosts;
    private float credits;

    public HeriaPlayer(UUID id, String name, HeriaRank rank, long firstConnection, String clientBrand, String connectedTo,
                       UUID reply, boolean removedTag, boolean mod, NickPlayerData nickData, List<UUID> friends,
                       List<UUID> pendingFriendsRequests, List<UUID> sentFriendsRequests, float coins, int hosts, float credits) {

        this.id = id;
        this.name = name;
        this.rank = rank;
        this.firstConnection = firstConnection;
        this.clientBrand = clientBrand;
        this.connectedTo = connectedTo;
        this.reply = reply;
        this.removedTag = removedTag;
        this.mod = mod;
        this.nickData = nickData;
        this.friends = friends;
        this.pendingFriendsRequests = pendingFriendsRequests;
        this.sentFriendsRequests = sentFriendsRequests;
        this.coins = coins;
        this.hosts = hosts;
        this.credits = credits;
    }

    public UUID getId() {
        return id;
    }

    public HeriaPlayer setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getNickedName(){
        if(!isNicked()){
            return getName();
        }

        return nickData.getNewName();
    }

    public HeriaPlayer setName(String name) {
        this.name = name;
        return this;
    }


    public HeriaRank getRank() {
        return rank;
    }

    public HeriaPlayer setRank(HeriaRank rank) {
        this.rank = rank;
        return this;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public HeriaPlayer setConnectedTo(String connectedTo) {
        this.connectedTo = connectedTo;
        return this;
    }

    public boolean isConnected(){
        return this.connectedTo != null;
    }

    public float getCoins() {
        return coins;
    }

    public HeriaPlayer setCoins(float coins) {
        this.coins = coins;
        return this;
    }

    public int getHosts() {
        return hosts;
    }

    public HeriaPlayer setHosts(int hosts) {
        this.hosts = hosts;
        return this;
    }

    public float getCredits() {
        return credits;
    }

    public void removeCredits(float credits){
        this.credits -= credits;
    }

    public HeriaPlayer setCredits(int credits) {
        this.credits = credits;
        return this;
    }

    public UUID getReply() {
        return reply;
    }

    public HeriaPlayer setReply(UUID reply) {
        this.reply = reply;
        return this;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public HeriaPlayer addFriend(UUID friends) {
        this.friends.add(friends);
        return this;
    }

    public List<UUID> getPendingFriendsRequests() {
        return pendingFriendsRequests;
    }

    public HeriaPlayer addPendingFriendsRequest(UUID pendingFriendsRequest) {
        this.pendingFriendsRequests.add(pendingFriendsRequest);
        return this;
    }

    public List<UUID> getSentFriendsRequests() {
        return sentFriendsRequests;
    }

    public HeriaPlayer addSentFriendsRequest(UUID sentFriendsRequest) {
        this.sentFriendsRequests.add(sentFriendsRequest);
        return this;
    }

    public long getFirstConnection() {
        return firstConnection;
    }

    public HeriaPlayer setFirstConnection(long firstConnection) {
        this.firstConnection = firstConnection;
        return this;
    }

    public String getClientBrand() {
        return clientBrand;
    }

    public HeriaPlayer setClientBrand(String clientBrand) {
        this.clientBrand = clientBrand;
        return this;
    }

    public boolean isRemovedTag() {
        return removedTag;
    }

    public HeriaPlayer setRemovedTag(boolean removedTag) {
        this.removedTag = removedTag;
        return this;
    }

    public boolean isMod() {
        return mod;
    }

    public HeriaPlayer setMod(boolean mod) {
        this.mod = mod;
        return this;
    }

    public boolean isNicked(){
        return nickData != null;
    }

    public NickPlayerData getNickData() {
        return nickData;
    }

    public HeriaPlayer setNickData(NickPlayerData nickData) {
        this.nickData = nickData;
        return this;
    }

    @Override
    public UUID getIdentifier() {
        return this.id;
    }

    @Override
    public void setIdentifier(UUID identifier) {
        this.id = identifier;
    }
}
