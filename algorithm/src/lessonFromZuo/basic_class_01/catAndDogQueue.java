package lessonFromZuo.basic_class_01;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 猫狗队列
 */
public class catAndDogQueue {
    public static class Pet {
        private String type;

        public Pet(String type) {
            this.type = type;
        }

        public String getPetType() {
            return this.type;
        }
    }

    public static class Dog extends Pet {
        public Dog() {
            super("dog");
        }
    }

    public static class Cat extends Pet {
        public Cat() {
            super("cat");
        }
    }

    public static class PetEnter {
        private Pet pet;
        private int count;

        public PetEnter(Pet pet, int count) {
            this.pet = pet;
            this.count = count;
        }

        public Pet getPet() {
            return this.pet;
        }


        public int getCount() {
            return this.count;
        }

//        public String getPetEnterType;
//
//        {
//            return this.pet.getPetType();
//        }
   }

    public static class CatDogQueue {
        private Queue<PetEnter> DogQ;
        private Queue<PetEnter> CatQ;
        private int count;

        public CatDogQueue() {
            this.DogQ = new LinkedList<PetEnter>();
            this.CatQ = new LinkedList<PetEnter>();
            this.count = 0;
        }

        public void addPet(Pet pet) {
            if (pet.getPetType().equals("dog")) {
                this.DogQ.add(new PetEnter(pet, count++));
            } else if (pet.getPetType().equals("cat")) {
                this.CatQ.add(new PetEnter(pet, count++));
            } else {
                throw new RuntimeException("err,it's not dog or cat");
            }
        }

        public Pet pollPet() {
            if (!this.DogQ.isEmpty() && !this.CatQ.isEmpty()) {
                if (this.CatQ.peek().getCount() < this.DogQ.peek().getCount()) {
                    return this.CatQ.poll().getPet();
                } else {
                    return this.DogQ.poll().getPet();
                }
            } else if (!isCatQueueEmpty()) {
                return this.CatQ.poll().getPet();
            } else if (!isDogQueueEmpty()) {
                return this.CatQ.poll().getPet();
            } else {
                throw new RuntimeException("err,there is no Dog or Cat");
            }
        }

        public Dog pollDog() {
            if (!isDogQueueEmpty()) {
                return (Dog) this.DogQ.poll().getPet();
            } else {
                throw new RuntimeException("err,there is no Dog");
            }
        }

        public Cat pollCat() {
            if (!isCatQueueEmpty()) {
                return (Cat) this.CatQ.poll().getPet();
            } else {
                throw new RuntimeException("err,there is no Dog");
            }
        }

        public boolean isDogQueueEmpty() {
            return this.DogQ.isEmpty();
        }

        public boolean isCatQueueEmpty() {
            return this.CatQ.isEmpty();
        }

        public boolean isCatDogQueueEmpty() {
            return this.DogQ.isEmpty() && this.CatQ.isEmpty();
        }
    }

    public static void main(String[] args) {
        CatDogQueue test = new CatDogQueue();

        Pet dog1 = new Dog();
        Pet cat1 = new Cat();
        Pet dog2 = new Dog();
        Pet cat2 = new Cat();
        Pet dog3 = new Dog();
        Pet cat3 = new Cat();

        test.addPet(dog1);
        test.addPet(cat1);
        test.addPet(dog2);
        test.addPet(cat2);
        test.addPet(dog3);
        test.addPet(cat3);

        test.addPet(dog1);
        test.addPet(cat1);
        test.addPet(dog2);
        test.addPet(cat2);
        test.addPet(dog3);
        test.addPet(cat3);

        test.addPet(dog1);
        test.addPet(cat1);
        test.addPet(dog2);
        test.addPet(cat2);
        test.addPet(dog3);
        test.addPet(cat3);
        while (!test.isDogQueueEmpty()) {
            System.out.println(test.pollDog().getPetType());
        }
        while (!test.isCatDogQueueEmpty()) {
            System.out.println(test.pollPet().getPetType());
        }
    }
}
