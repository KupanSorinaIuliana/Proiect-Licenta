package com.example.licenta.helpers;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.licenta.model.User;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static java.lang.Integer.parseInt;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c=null;
    private DatabaseAccess(Context context){
        this.openHelper= new DatabaseHelper(context);
    }
    public static  DatabaseAccess getInstance(Context context){
        if(instance==null){
            instance= new DatabaseAccess(context);
        }
        return instance;
    }
    public void open(){
        this.db=openHelper.getWritableDatabase();
    }
    public void close(){
        if(db!=null){
            this.db.close();
        }
    }

    //PRELUARI DATE DESPRE CAROSERII

    //preluare denumiri din tabelul UTILIZARE pentru id-ul oferit ca si argument
    public String getDenumireUtilizare(String id) {
        c = db.rawQuery("select denumire from utilizare where id='" + id + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String denumire = c.getString(0);
            buffer.append("" + denumire);
        }
        return buffer.toString();
    }
    //preluare denumiri din tabelul CAROSERII pentru id-ul oferit ca si argument
    public String getDenumireCaroserie(int id) {
        c = db.rawQuery("select denumire from caroserii where id='" + id + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String denumire = c.getString(0);
            buffer.append("" + denumire);
        }
        return buffer.toString();
    }
    //preluare denumiri din tabelul BUGET pentru id-ul oferit ca si argument
    public String getDenumireBuget(String id){
        c = db.rawQuery("select denumire from buget where id='" + id + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String denumire = c.getString(0);
            buffer.append("" + denumire);
        }
        return buffer.toString();
    }
    //preluare denumiri din tabelul COMBUSTIBIL pentru id-ul oferit ca si argument
    public String getDenumireCombustibil(String id){
        c = db.rawQuery("select denumire from combustibil where id='" + id + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String denumire = c.getString(0);
            buffer.append("" + denumire);
        }
        return buffer.toString();
    }
    //preluare denumiri din tabelul SEX pentru id-ul oferit ca si argument
    public String getDenumireSex(String id) {
        c = db.rawQuery("select denumire from sex where id='" + id + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String denumire = c.getString(0);
            buffer.append("" + denumire);
        }
        return buffer.toString();
    }
    //preluare denumiri din tabelul EXPERIENTA pentru id-ul oferit ca si argument
    public String getDenumireExperienta(String id) {
        c = db.rawQuery("select denumire from experienta where id='" + id + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String denumire = c.getString(0);
            buffer.append("" + denumire);
        }
        return buffer.toString();
    }
    //preluare denumiri din tabelul COPII pentru id-ul oferit ca si argument
    public String getCopii(int id) {
        String denumiri=new String();
        String selectQuery = "SELECT denumire FROM copii WHERE id="+id;
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                denumiri = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return denumiri;
    }
    //preluare id din tabelul CAROSERII pentru caroseria oferite ca si argument
    public int getCaroserieId(String denumire) {
        c = db.rawQuery("select id from caroserii where denumire='" + denumire + "'", new String[]{});
        int id=0;
        while(c.moveToNext()){
            id= c.getInt(0);
        }
        return id;
    }
    //preluare id pentru marca oferita ca si argument
    public int getMarcaId(String denumire) {
        c = db.rawQuery("select id from marci where denumire='" + denumire + "'", new String[]{});
        int id=0;
        while(c.moveToNext()){
            id= c.getInt(0);
        }
        return id;
    }
    //preluarea denumirilor tuturor caroseriilor
    public List<String> getCaroserii() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        List<String> denumiri = new ArrayList<String>();
        String selectQuery = "SELECT denumire FROM caroserii";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(0);
                denumiri.add(s);
            } while (cursor.moveToNext());
        }
        return denumiri;
    }
    //preluare date despre caroseria oferite ca si argument
    public ArrayList getCaroserie(String denumire) {
        c = db.rawQuery("select firma, ute_id, cpi_id, concediu, sex_id, exp_id, principala from caroserii where denumire='" + denumire + "'", new String[]{});
        ArrayList lista = new ArrayList();
        while (c.moveToNext()) {
            int firma = c.getInt(0);
            String utilizare = c.getString(1);
            int familie = c.getInt(2);
            int concediu = c.getInt(3);
            String sex = c.getString(4);
            String experienta = c.getString(5);
            int principala = c.getInt(6);
            lista.add(firma);
            lista.add(utilizare);
            lista.add(familie);
            lista.add(concediu);
            lista.add(sex);
            lista.add(experienta);
            lista.add(principala);
        }
        return lista;
    }
    //preluare date despre modelul oferit ca si argument
    public ArrayList getModeleDetalii(int id) {
        c = db.rawQuery("select md.denumire, md.poza, cr.denumire, mc.denumire, md.anul, md.cai_putere, md.capacitate_cilindrica, bg.denumire, cb.denumire from modele md join caroserii cr on md.cri_id=cr.id join marci mc on md.mci_id=mc.id join buget bg on md.bgt_id=bg.id join combustibil cb on md.cbl_id=cb.id where md.id=" + id , new String[]{});
        ArrayList lista = new ArrayList();
        while (c.moveToNext()) {
            String model=c.getString(0);
            String poza=c.getString(1);
            String caroserie = c.getString(2);
            String marca = c.getString(3);
            String anul = c.getString(4);
            String cai_putere = c.getString(5);
            String capacitate_cilindrica = c.getString(6);
            String buget = c.getString(7);
            String combustibil = c.getString(8);
            lista.add(model);
            lista.add(poza);
            lista.add(caroserie);
            lista.add(buget);
            lista.add(combustibil);
            lista.add(marca);
            lista.add(anul);
            lista.add(cai_putere);
            lista.add(capacitate_cilindrica);
        }
        return lista;
    }
    //preluare id-uri modele
    public ArrayList getModeletot(String caroserie, String buget, String combustibil){
        c=db.rawQuery("select md.id from modele md join caroserii cr  join buget bg join combustibil cb ON md.cri_id=cr.id AND md.bgt_id=bg.id AND md.cbl_id=cb.id where cr.denumire='" + caroserie + "' AND bg.denumire='"+buget+"' AND cb.denumire='"+combustibil+"'", new String[]{});
        ArrayList lista = new ArrayList();
    while (c.moveToNext()) {
            int model=c.getInt(0);
            lista.add(model);
        }
        return lista;
    }
    //preluare statistici pentru user-ul dat ca si argument
    public ArrayList getStatistici(User user){
        c=db.rawQuery("select id from statistici where user_id='" + user.getId() + "'", new String[]{});
        ArrayList lista = new ArrayList();
        //, cr.denumire, mc.denumire, md.anul, md.cai_putere, md.capacitate_cilindrica, bg.denumire, cb.denumire
        while (c.moveToNext()) {
            int id=c.getInt(0);
            lista.add(id);
        }
        return lista;
    }
    ////selectarea id-urilor caroseriilor din statistici pentru realizarea graficului de popularitate a caroseriilor
    public ArrayList getNumberCaroserii() {
        c = db.rawQuery("select caroserie_id from statistici" , new String[]{});
        ArrayList lista = new ArrayList();
        while (c.moveToNext()) {
            int id=c.getInt(0);
            lista.add(id);
        }
        return lista;
    }
    private static final String COLUMN_IDUSER = "usr_id";
    private static final String COLUMN_MODELEID="mde_id";
    private static final String TABLE_MODELE_SALVATE="modele_salvate";
    //adaugarea modelului ales de utilizator
    public void addCars(User user, int idCar){
        ContentValues values= new ContentValues();
        values.put(COLUMN_IDUSER, user.getId());
        values.put(COLUMN_MODELEID, idCar);

        db.insert(TABLE_MODELE_SALVATE, null, values);
    }
    //stergerea unui model
    public void deleteCar(User user, int idCar){
        db.delete(TABLE_MODELE_SALVATE, COLUMN_IDUSER + " = "+ user.getId() +" AND "+COLUMN_MODELEID +"="+idCar,
                new String[]{});
    }
    //preluarea modelelor salvate de catre un utilizator
    public ArrayList getCars(User user){
        c=db.rawQuery("select mde_id from modele_salvate where usr_id='" + user.getId() + "'", new String[]{});
        ArrayList lista = new ArrayList();
        while (c.moveToNext()) {
            int model=c.getInt(0);
            lista.add(model);
        }
        return lista;
    }
    //preluare denumiri din tabelul UTILIZARE
    public List<String> getUtilizare() {
        List<String> denumiri = new ArrayList<String>();
        String selectQuery = "SELECT denumire FROM utilizare";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(0);
                denumiri.add(s);
            } while (cursor.moveToNext());
        }
        return denumiri;
    }
    //preluarea tuturor denumirilor marcilor
    public List<String> getMarci() {
        List<String> denumiri = new ArrayList<String>();
        String selectQuery = "SELECT denumire FROM marci";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(0);
                denumiri.add(s);
            } while (cursor.moveToNext());
        }
        return denumiri;
    }
    //preluarea logo-ului marcii date ca si argument
    public String getLogo(String marca) {
        String logo= new String();
        String selectQuery = "SELECT logo FROM marci WHERE denumire='"+marca+"'";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                logo = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return logo;
    }
    private static final String TABLE_STATISTICI="statistici";
    private static final String COLUMN_STATISTICI_ID="id";
    private static final String COLUMN_STATISTICI_PROCENT="procent";
    private static final String COLUMN_STATISTICI_CAROSERIE="caroserie_id";
    private static final String COLUMN_STATISTICI_USER="user_id";
    public int verificaStatistici(int user, int cri_id){
        c = db.rawQuery("select count(id) from statistici where caroserie_id="+ cri_id + " and user_id="+ user , new String[]{});
        int var=0;
        while (c.moveToNext()) {
            if(c.getInt(0)>0)
               var=1;
        }
        return var;
    }
    public void updateStatistici(User user, int cri_id, double procent){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATISTICI_PROCENT, procent);
        db.update(TABLE_STATISTICI, values, COLUMN_STATISTICI_USER + " = ? AND "+COLUMN_STATISTICI_CAROSERIE+"=?",
                new String[]{String.valueOf(user.getId()), String.valueOf(cri_id)});
    }
    //adaugare rezultate in tabelul STATISTICI
    public void addStatistici(User user, double procent, int caroserie){
        ContentValues values= new ContentValues();
        values.put(COLUMN_STATISTICI_USER, user.getId());
        values.put(COLUMN_STATISTICI_PROCENT, procent);
        values.put(COLUMN_STATISTICI_CAROSERIE, caroserie);

        db.insert(TABLE_STATISTICI, null, values);
    }
    //preluare datelor despre statistica oferita ca si argument
    public ArrayList getStatisticiTot(int id) {
        c = db.rawQuery("select st.procent, cr.id from statistici st join caroserii cr ON st.caroserie_id=cr.id where st.id=" + id , new String[]{});
        ArrayList lista = new ArrayList();
        while (c.moveToNext()) {
            int procente=c.getInt(0);
            int caroserie=c.getInt(1);
            lista.add(procente);
            lista.add(caroserie);
        }
        return lista;
    }

    //ADMINISTRARE USER, GESTIONARE CONT
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "parola";
    private static final String COLUMN_USER_NUME="nume";
    private static final String COLUMN_USER_PRENUME="prenume";
    private static final String COLUMN_USER_AGE="varsta";
    private static final String TABLE_USER="useri";

    //criptare parola
    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    //adaugare user in baza de date
    public void addUser(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, generateStrongPasswordHash(user.getPassword()));
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_NUME, user.getNume());
        values.put(COLUMN_USER_PRENUME, user.getPrenume());
        values.put(COLUMN_USER_AGE, user.getAge());

        db.insert(TABLE_USER, null, values);
        db.close();
    }
    //preluare id user
    public int getUserId(String email){
        String[] columns={
                COLUMN_USER_ID
        };
        String sortOrder =
                COLUMN_USER_ID + " ASC";
        String selection = COLUMN_USER_EMAIL + " = ?";//argument de selectie
        String[] selectionArgs = {email};//valoare argument
        Cursor cursor = db.query(TABLE_USER,
                columns,    //coloane de returnat
                selection,        //coloane pentru clauza WHERE
                selectionArgs,        //valori pentru clauza WHERE
                null,       //grupeaza randurile
                null,
                sortOrder);
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user.setId(parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return user.getId();
    }
    //preluare id user
    public String getUsername(String email){
        String[] columns={
                COLUMN_USER_USERNAME
        };
        String sortOrder =
                COLUMN_USER_USERNAME + " ASC";
        String selection = COLUMN_USER_EMAIL + " = ?";//argument de selectie
        String[] selectionArgs = {email};//valoare argument
        Cursor cursor = db.query(TABLE_USER,
                columns,    //coloane de returnat
                selection,        //coloane pentru clauza WHERE
                selectionArgs,        //valori pentru clauza WHERE
                null,       //grupeaza randurile
                null,
                sortOrder);
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return user.getUsername();
    }
    //preluarea tuturor datelor despre user
    public User getAllUser(String email) {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NUME,
                COLUMN_USER_PRENUME,
                COLUMN_USER_AGE
        };
        String sortOrder =
                COLUMN_USER_NUME + " ASC";//ordine de sortare
        List<User> userList = new ArrayList<User>();
        String selection = COLUMN_USER_EMAIL+ "=?";//coloana de selectie
        String[] selectionArgs = {email};//argumentul de selectie

        //SELECT id, nume, prenume, username, email, password FROM useri ORDER BY nume;
        Cursor cursor = db.query(TABLE_USER, //tabel pentru query
                columns,    //coloane de returnat
                selection,        //coloane pentru clauza WHERE
                selectionArgs,        //valori pentru clauza WHERE
                null,       //grupare randuri
                null,       //filtrare dupa gruparea randurilor
                sortOrder); //ordinea de sortare
        User user = new User();
        if (cursor.moveToFirst()) {// traversare randuri si adaugare in lista
            do {
                user.setId(parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setNume(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NUME)));
                user.setPrenume(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PRENUME)));
                user.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_AGE)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return user;//returnare lista
    }

    //update pentru toate datele ale userului
    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_NUME, user.getNume());
        values.put(COLUMN_USER_PRENUME, user.getPrenume());
        values.put(COLUMN_USER_AGE, user.getAge());
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    //update pentru nume
    public void updateUserNume(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NUME, user.getNume());
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }
    //update pentru data nasterii
    public void updateUserDate(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_AGE, user.getNume());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }
    //update pentru prenume
    public void updateUserPrenume(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PRENUME, user.getPrenume());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }
    //update pentru username
    public void updateUserUsername(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }
    //update pentru age
    public void updateUserAge(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_AGE, user.getUsername());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }
    //realizare update pentru parola
    public void updateUserParola(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        ContentValues values= new ContentValues();
        values.put(COLUMN_USER_PASSWORD, generateStrongPasswordHash(user.getPassword()));
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL+ " =?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }
    //stergere de user
    public void deleteUser(User user) {
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    //verificare daca exista acest cont in baza de date
    public boolean checkUser(String email) {
        db= openHelper.getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID
        };//coloane de returnat
        String selection = COLUMN_USER_EMAIL + " = ?";//criteriul de selectie
        String[] selectionArgs = {email};//argumentul pentru selectie
        // query user table with condition
        //SELECT column_user_id FROM useri WHERE column_user_email = 'jack@androidtutorialshub.com';
        Cursor cursor = db.query(TABLE_USER, //tabel pentru query
                columns,                    //coloane de returnat
                selection,                  //coloane pentru clauze WHERE
                selectionArgs,              //valori pentru clauza WHERE
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //ordinea de sortare
        int cursorCount = cursor.getCount();
        cursor.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //verificare parola
    public boolean checkUser(String email, String parola) throws InvalidKeySpecException, NoSuchAlgorithmException {
        db=openHelper.getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_PASSWORD
        };//coloanele de returnat
        String selection = COLUMN_USER_EMAIL + " = ?" ;//criteriul de selectie
        String[] selectionArgs = {email};//argumentul pentru selectie
        // query user table with conditions
        //SELECT column_user_id, column_user_password FROM useri WHERE column_user_email = 'jack@androidtutorialshub.com';
        Cursor cursor = db.query(TABLE_USER, //tabel pentru query
                columns,                    //coloanele de returnat
                selection,                  //coloanele pentru clauza WHERE
                selectionArgs,              //valorile pentru clauza WHERE
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //ordinea de sortare
        int cursorCount = cursor.getCount();
        String pass="";
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                do {
                    pass=cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
                } while (cursor.moveToNext());
            }
            cursor.close();
            if(validatePassword(parola,pass)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    //transformare in hash si comparare cu valoarea din baza de date
    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    //transformare din hex in byte
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
