package com.mcmoddev.communitymod.jamieswhiteshirt.primestacks;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.SortedSet;
import java.util.TreeSet;

@SubMod(
    name = "Prime Stacks",
    description = "Rounds the default max stack size to the previous prime number",
    attribution = "JamiesWhiteShirt"
)
public class PrimeStacks implements ISubMod {
    private static int nextPrimeCandidate = 3;
    private static SortedSet<Integer> primes = new TreeSet<>();

    static {
        primes.add(2);
    }

    private static boolean testPrimality(int pc) {
        for (int p : primes) {
            if ((pc % p) == 0) {
                return false;
            }
        }
        return true;
    }

    private static int getPreviousPrime(int n) {
        if (n <= 1) return n;

        // Generate a new prime if necessary
        for (; nextPrimeCandidate <= n; nextPrimeCandidate++) {
            if (testPrimality(nextPrimeCandidate)) {
                primes.add(nextPrimeCandidate);
            }
        }

        return primes.headSet(n + 1).last(); // retrieve previous prime
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegisterItems(RegistryEvent.Register<Item> e) {
        for (Item item : e.getRegistry().getValuesCollection()) {
            item.setMaxStackSize(getPreviousPrime(item.getItemStackLimit()));
        }
    }
}
