package lessonFromZuo.basic_class_03;

import java.util.HashMap;

public class randomPool<K> {
    public HashMap<K, Integer> keyIndexMap;
    public HashMap<Integer, K> indexKeyMap;
    public int size;

    /**
     * 初始化
     */
    public randomPool() {
        this.keyIndexMap = new HashMap<K, Integer>();
        this.indexKeyMap = new HashMap<Integer, K>();
        this.size = 0;
    }

    public void insert(K key) {
        if (!this.keyIndexMap.containsKey(key)) {
            this.keyIndexMap.put(key, this.size);
            this.indexKeyMap.put(this.size, key);
            this.size++;
        }
    }

    public K getRandom() {
        if (this.size == 0) {
            return null;
        }
        int a = (int) (Math.random() * this.size);

        return this.indexKeyMap.get(a);
    }

    public String delete(K key) {
        if (this.keyIndexMap.containsKey(key)) {
            int deleteIndex = keyIndexMap.get(key);
            int lastIndex = this.size-1;
            K lastKey = indexKeyMap.get(lastIndex);


            indexKeyMap.put(deleteIndex, lastKey);
            keyIndexMap.put(lastKey, deleteIndex);

            this.keyIndexMap.remove(key);
            this.indexKeyMap.remove(lastIndex);

            this.size=this.size-1;

            return "成功删除";

        } else {
            return "无相应值";
        }
    }

    public static void main(String[] args) {
        randomPool<String> pool = new randomPool<String>();
        pool.insert("1");
        pool.insert("2");
        pool.insert("3");
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());

        System.out.println(pool.delete("1"));

        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());

        System.out.println(pool.getRandom());

        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.delete("2"));
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());

        System.out.println(pool.getRandom());

        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());


    }

}
