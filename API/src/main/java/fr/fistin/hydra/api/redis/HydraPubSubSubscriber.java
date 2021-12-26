package fr.fistin.hydra.api.redis;

import fr.fistin.hydra.api.redis.receiver.IHydraChannelReceiver;
import fr.fistin.hydra.api.redis.receiver.IHydraPatternReceiver;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:48
 */
class HydraPubSubSubscriber extends JedisPubSub {

    private final Map<String, Set<IHydraChannelReceiver>> channelsReceivers;
    private final Map<String, Set<IHydraPatternReceiver>> patternsReceivers;

    public HydraPubSubSubscriber() {
        this.channelsReceivers = new HashMap<>();
        this.patternsReceivers = new HashMap<>();
    }

    public void registerReceiver(String channel, IHydraChannelReceiver receiver) {
        final Set<IHydraChannelReceiver> receivers = this.channelsReceivers.get(channel) != null ? this.channelsReceivers.get(channel) : ConcurrentHashMap.newKeySet();

        receivers.add(receiver);

        this.channelsReceivers.put(channel, receivers);
    }

    public void unregisterReceiver(String channel, IHydraChannelReceiver receiver) {
        try {
            final Set<IHydraChannelReceiver> receivers = this.channelsReceivers.get(channel);

            if (receivers != null && receivers.contains(receiver)) {
                receivers.remove(receiver);

                this.channelsReceivers.put(channel, receivers);
            }
        } catch (Exception ignored) {}
    }

    public void registerReceiver(String pattern, IHydraPatternReceiver receiver) {
        final Set<IHydraPatternReceiver> receivers = this.patternsReceivers.get(pattern) != null ? this.patternsReceivers.get(pattern) : ConcurrentHashMap.newKeySet();

        receivers.add(receiver);

        this.patternsReceivers.put(pattern, receivers);
    }

    public void unregisterReceiver(String channel, IHydraPatternReceiver receiver) {
        try {
            final Set<IHydraPatternReceiver> receivers = this.patternsReceivers.get(channel);

            if (receivers != null && receivers.contains(receiver)) {
                receivers.remove(receiver);

                this.patternsReceivers.put(channel, receivers);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onMessage(String channel, String message) {
        final Set<IHydraChannelReceiver> receivers = this.channelsReceivers.get(channel);

        if (receivers != null) {
            receivers.forEach(receiver -> receiver.receive(channel, message));
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        final Set<IHydraPatternReceiver> receivers = this.patternsReceivers.get(pattern);

        if (receivers != null) {
            receivers.forEach(receiver -> receiver.receive(pattern, channel, message));
        }
    }

    public Set<String> getChannelsSubscribed() {
        return this.channelsReceivers.keySet();
    }

    public Set<String> getPatternsSubscribed() {
        return this.patternsReceivers.keySet();
    }

}
