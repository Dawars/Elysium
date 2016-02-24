package elysium.common.lib.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by dawar on 2016. 02. 02..
 */

public class BlockStateUtils {
    public BlockStateUtils() {
    }

    public static EnumFacing getFacing(IBlockState state) {
        return EnumFacing.getFront(state.getBlock().getMetaFromState(state) & 7);
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }

    public static boolean isEnabled(IBlockState state) {
        return (state.getBlock().getMetaFromState(state) & 8) != 8;
    }

    public static boolean isEnabled(int meta) {
        return (meta & 8) != 8;
    }

    public static IProperty getPropertyByName(IBlockState blockState, String propertyName) {
        Iterator i$ = blockState.getProperties().keySet().iterator();

        IProperty property;
        do {
            if(!i$.hasNext()) {
                return null;
            }

            property = (IProperty)i$.next();
        } while(!property.getName().equals(propertyName));

        return property;
    }

    public static boolean isValidPropertyName(IBlockState blockState, String propertyName) {
        return getPropertyByName(blockState, propertyName) != null;
    }

    public static Comparable getPropertyValueByName(IBlockState blockState, IProperty property, String valueName) {
        Iterator i$ = ((ImmutableSet)property.getAllowedValues()).iterator();

        Comparable value;
        do {
            if(!i$.hasNext()) {
                return null;
            }

            value = (Comparable)i$.next();
        } while(!value.toString().equals(valueName));

        return value;
    }

    public static ImmutableSet<IBlockState> getValidStatesForProperties(IBlockState baseState, IProperty... properties) {
        if(properties == null) {
            return null;
        } else {
            HashSet validStates = Sets.newHashSet();
            BlockStateUtils.PropertyIndexer propertyIndexer = new BlockStateUtils.PropertyIndexer(properties);

            do {
                IBlockState currentState = baseState;
                IProperty[] arr$ = properties;
                int len$ = properties.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    IProperty property = arr$[i$];
                    BlockStateUtils.IndexedProperty indexedProperty = propertyIndexer.getIndexedProperty(property);
                    currentState = currentState.withProperty(property, indexedProperty.getCurrentValue());
                }

                validStates.add(currentState);
            } while(propertyIndexer.increment());

            return ImmutableSet.copyOf(validStates);
        }
    }

    private static class IndexedProperty {
        private ArrayList<Comparable> validValues;
        private int maxCount;
        private int counter;
        private BlockStateUtils.IndexedProperty parent;
        private BlockStateUtils.IndexedProperty child;

        private IndexedProperty(IProperty property) {
            this.validValues = new ArrayList();
            this.validValues.addAll(property.getAllowedValues());
            this.maxCount = this.validValues.size() - 1;
        }

        public boolean increment() {
            if(this.counter < this.maxCount) {
                ++this.counter;
                return true;
            } else if(this.hasParent()) {
                this.resetSelfAndChildren();
                return this.parent.increment();
            } else {
                return false;
            }
        }

        public void resetSelfAndChildren() {
            this.counter = 0;
            if(this.hasChild()) {
                this.child.resetSelfAndChildren();
            }

        }

        public boolean hasParent() {
            return this.parent != null;
        }

        public boolean hasChild() {
            return this.child != null;
        }

        public int getCounter() {
            return this.counter;
        }

        public int getMaxCount() {
            return this.maxCount;
        }

        public Comparable getCurrentValue() {
            return (Comparable)this.validValues.get(this.counter);
        }
    }

    private static class PropertyIndexer {
        private HashMap<IProperty, IndexedProperty> indexedProperties;
        private IProperty finalProperty;

        private PropertyIndexer(IProperty... properties) {
            this.indexedProperties = new HashMap();
            this.finalProperty = properties[properties.length - 1];
            BlockStateUtils.IndexedProperty previousIndexedProperty = null;
            IProperty[] arr$ = properties;
            int len$ = properties.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                IProperty property = arr$[i$];
                BlockStateUtils.IndexedProperty indexedProperty = new BlockStateUtils.IndexedProperty(property);
                if(previousIndexedProperty != null) {
                    indexedProperty.parent = previousIndexedProperty;
                    previousIndexedProperty.child = indexedProperty;
                }

                this.indexedProperties.put(property, indexedProperty);
                previousIndexedProperty = indexedProperty;
            }

        }

        public boolean increment() {
            return ((BlockStateUtils.IndexedProperty)this.indexedProperties.get(this.finalProperty)).increment();
        }

        public BlockStateUtils.IndexedProperty getIndexedProperty(IProperty property) {
            return (BlockStateUtils.IndexedProperty)this.indexedProperties.get(property);
        }
    }
}
