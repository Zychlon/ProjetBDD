package bdd;

import java.io.*;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Classe qui contient des outils de sérialization
 *
 * @author Jason Mahdjoub
 * @version 1.0
 */
class SerializationTools {
    /**
     * Serialise/binarise l'objet passé en paramètre pour retourner un tableau binaire
     *
     * @param o l'objet à serialiser
     * @return the tableau binaire
     * @throws IOException si un problème d'entrée/sortie se produit
     */
    static byte[] serialize(Serializable o) throws IOException {
        if (o != null) {    //  Vérification de l'objet à serialiser
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);   //  Init classe de serialisation
            oos.writeObject(o); //  Ecriture du sérialisable
            oos.flush();    //  flush
            return bos.toByteArray();   //  Retour de la Sérialisation
        } else
        	throw new NullPointerException();   //  Message d'erreur
    }

    /**
     * Désérialise le tableau binaire donné en paramètre pour retrouver l'objet initial avant sa sérialisation
     *
     * @param data le tableau binaire
     * @return l'objet désérialisé
     * @throws IOException            si un problème d'entrée/sortie se produit
     * @throws ClassNotFoundException si un problème lors de la déserialisation s'est produit
     */
    static Serializable deserialize(byte[] data) throws IOException, ClassNotFoundException {
        if (data != null) { //  Si la chaine envoyée est non nulle
            ByteArrayInputStream ba = new ByteArrayInputStream(data);
            ObjectInputStream obj = new ObjectInputStream(ba);  //  Init de la classe de sérialisation
            return (Serializable) obj.readObject(); //  Déserialisation
        } else
            throw new NullPointerException();   //  Message d'erreur
    }

    /**
     * Serialise/binarise le tableau d'espaces libres passé en paramètre pour retourner un tableau binaire, mais selon le schéma suivant :
     * Pour chaque interval ;
     * <ul>
     *     <li>écrire en binaire la position de l'interval</li>
     *     <li>écrire en binaire la taille de l'interval</li>
     * </ul>
     * Utilisation pour cela la classe {@link DataOutputStream}
     *
     * @param freeSpaceIntervals le tableau d'espaces libres
     * @return un tableau binaire
     * @throws IOException si un problème d'entrée/sortie se produit
     */
    static byte[] serializeFreeSpaceIntervals(TreeSet<BDD.FreeSpaceInterval> freeSpaceIntervals) throws IOException {
        if (freeSpaceIntervals != null) {   //  Si la chaine passée est non nulle
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);  //  Init classe de la sérialisation
            for(BDD.FreeSpaceInterval spaceInterval : freeSpaceIntervals){  // pour chaque espace vide
                dos.writeLong(spaceInterval.getStartPosition());    //  Création d'espace vide à la 1e position
                dos.writeLong(spaceInterval.getLength());    //  Création d'espace vide sur toute la longueur conrrespondante
                dos.flush();    //  flush
            }
            return baos.toByteArray();  //  renvoie de la sérialisation
        } else
            throw new NullPointerException();   //  Message d'erreur
    }

    /**
     * Effectue l'opération inverse de la fonction {@link #serializeFreeSpaceIntervals(TreeSet)}
     *
     * @param data le tableau binaire
     * @return le tableau d'espaces libres
     * @throws IOException si un problème d'entrée/sortie se produit
     */
    static TreeSet<BDD.FreeSpaceInterval> deserializeFreeSpaceIntervals(byte[] data) throws IOException {
        if(data != null) {   //  Si le bytearray passé est non null
            TreeSet<BDD.FreeSpaceInterval> freeSpaceInterval = new TreeSet<BDD.FreeSpaceInterval>();
            for(int i = 0; i < data.length; i += 16) {  //  Déserialisation des espaces vides 1 à 1
                ByteArrayInputStream tab = new ByteArrayInputStream(Arrays.copyOfRange(data, i, i + 8));
                DataInputStream obj = new DataInputStream(tab);  //  Init classe de la sérialisation
                ByteArrayInputStream tab_2 = new ByteArrayInputStream(Arrays.copyOfRange(data, i + 8, i + 16));
                DataInputStream obj_2 = new DataInputStream(tab);//  Init classe de la sérialisation
                freeSpaceInterval.add(new BDD.FreeSpaceInterval((long)tab.read(), (long)tab_2.read()));
                tab.close();
                obj.close();
            }
            return freeSpaceInterval;   //  Retour de l'espace vide déserialisé
        } else
            throw new NullPointerException();   //  Message d'erreur
    }
}
