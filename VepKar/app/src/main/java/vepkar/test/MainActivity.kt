package vepkar.test

import android.content.pm.ActivityInfo
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.widget.AdapterView.OnItemSelectedListener
import android.os.Bundle
import android.view.View
import android.widget.*
import java.io.IOException

private var textView3: TextView? = null
private var editText: EditText? = null
private var read: Button? = null
private var textView: TextView? = null
private var textView2: TextView? = null
private var textView4: TextView? = null
private var textView5: TextView? = null
private var pos: Int = 0
private var str:String = ""
private var str2:String = ""
var countries = arrayOf(
        "Язык не выбран", "Вепсcкий", "Карельский: ливвиковское наречие", "Карельский: людиковское наречие",
        "Карельский: собственно карельское наречие")

//Переменная для работы с БД
private var mDBHelper: DatabaseHelper? = null
private var mDb: SQLiteDatabase? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // запрет вращения экрана
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        mDBHelper = DatabaseHelper(this)
        try {
            mDBHelper?.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        try {
            mDb = mDBHelper?.getWritableDatabase()
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        textView4 = findViewById(R.id.textView4)
        textView5 = findViewById(R.id.textView5)
        read = findViewById(R.id.read)
        editText = findViewById(R.id.editText)

        // выбор языка
        val spinner = findViewById<View>(R.id.spinner) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.prompt = "Выберите язык"
        spinner.setSelection(0)
        // устанавливаем обработчик нажатия
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?, view: View,
                    position: Int, id: Long
            ) {
                // показываем позиция нажатого элемента
                //Toast.makeText(baseContext, "Position = $position", Toast.LENGTH_SHORT).show()
                pos = position
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        //Пропишем обработчик клика кнопки
        read?.setOnClickListener(View.OnClickListener {
            str = editText?.text.toString()
            str2 = str

            var count: Int = 0
            var result: String = "Найдено "
            var cursor: Cursor
            //Список
            val clients = ArrayList<HashMap<String, Any?>>()
            //Список параметров конкретного клиента
            var client: HashMap<String, Any?>

            //var cursor: Cursor = mDb!!.rawQuery("SELECT lemmas.lem, lang2.ru, pos.pos_ru FROM lemmas, lang2, pos WHERE lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id", null)
            //Отправляем запрос в БД
            if (str.isEmpty()) {
                if (pos == 0){
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru   FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id) UNION  SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id  FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id) LIMIT 25  ", null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id ) UNION SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id)  ", null)
                }
                else if (pos == 1) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru   FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='1') UNION  SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id  FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='1') LIMIT 25  ", null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='1') UNION  SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='1')  ", null)
                }
                else if (pos == 2) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru   FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='5') UNION  SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id  FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='5') LIMIT 25  ", null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='5') UNION  SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='5') ", null)
                } else if (pos == 3) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru   FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='6') UNION  SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id  FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='6') LIMIT 25  ", null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='6') UNION  SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='6') ", null)
                } else {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru   FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='4') UNION  SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id  FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='4') LIMIT 25  ", null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='4') UNION  SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='4') ", null)
                }
            } else {
                if (pos == 0) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and wordforms.wordform like ?) UNION SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.lem like ?) LIMIT 25", arrayOf(str,str2) ,null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and wordforms.wordform like ?) UNION SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lem like ?)", arrayOf(str,str2) ,null)
                } else if (pos == 1) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='1' and wordforms.wordform like ?) UNION SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='1' and lemmas.lem like ?) LIMIT 25", arrayOf(str,str2) ,null)
                    //cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_lang='1' and lemmas.id_pos=pos.id and wordforms.wordform like ?) UNION SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='1' and lemmas.lem like ?)", arrayOf(str,str2) ,null)
                } else if (pos == 2) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='5' and wordforms.wordform like ?) UNION SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='5' and lemmas.lem like ?) LIMIT 25", arrayOf(str,str2) ,null)
                    //cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_lang='5' and lemmas.id_pos=pos.id and wordforms.wordform like ?) UNION SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='5' and lemmas.lem like ?)", arrayOf(str,str2) ,null)
                } else if (pos == 3) {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='6' and wordforms.wordform like ?) UNION SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='6' and lemmas.lem like ?) LIMIT 25", arrayOf(str,str2) ,null)
                    // cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_lang='6' and lemmas.id_pos=pos.id and wordforms.wordform like ?) UNION SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='6' and lemmas.lem like ?)", arrayOf(str,str2) ,null)
                } else {
                    cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, langs.ru, lemmas.lem, lemmas.desc_ru, pos.ru FROM wordforms, lemmas, gramsets, langs, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=langs.id and wordforms.gramset_id=gramsets.id and lemmas.id_pos=pos.id and lemmas.id_lang='4' and wordforms.wordform like ?) UNION SELECT lemmas.lem, langs.ru, pos.ru, lemmas.desc_ru, langs.id, langs.id FROM lemmas, pos, langs WHERE (lemmas.id_lang=langs.id and lemmas.id_pos=pos.id and lemmas.id_lang='4' and lemmas.lem like ?) LIMIT 25", arrayOf(str,str2) ,null)
                    //cursor = mDb!!.rawQuery("SELECT wordforms.wordform, gramsets.ru, lang2.ru, lemmas.lem, lemmas.desc_ru, pos.pos_ru   FROM wordforms, lemmas, gramsets, lang2, pos WHERE (wordforms.lemma_id=lemmas.id and lemmas.id_lang=lang2.id and wordforms.gramset_id=gramsets.id and lemmas.id_lang='4' and lemmas.id_pos=pos.id and wordforms.wordform like ?) UNION SELECT lemmas.lem, lang2.ru, pos.pos_ru, lemmas.desc_ru, lang2.id, lang2.id FROM lemmas, pos, lang2 WHERE (lemmas.id_lang=lang2.id and lemmas.id_pos=pos.id and lemmas.id_lang='4' and lemmas.lem like ?)", arrayOf(str,str2) ,null)
                }
            }
            cursor.moveToFirst()
            var id_l = arrayOf(1,4,5,6)
            //Пробегаем по всем клиентам
            while (!cursor.isAfterLast) {
                client = HashMap()
                count += 1
                if (cursor.getString(4) == id_l[0].toString() || cursor.getString(4) == id_l[1].toString() || cursor.getString(4) == id_l[2].toString() || cursor.getString(4) == id_l[3].toString() ) {
                    client["lem"] = cursor.getString(0)
                    client["lang"] = cursor.getString(1)
                    client["pos"] = cursor.getString(2)
                    client["desc"] = cursor.getString(3)
                } else{
                    client["lem"] = cursor.getString(0)
                    client["lang"] = cursor.getString(2)
                    client["pos"] = cursor.getString(5) + ", "+ cursor.getString(1)
                    if (cursor.getString(4) != null) {
                        client["desc"] = "Начальная форма: "+ cursor.getString(3) + '\n'+ cursor.getString(4)
                    } else {
                        client["desc"] = "Начальная форма: "+ cursor.getString(3) }

                }
                //Закидываем клиента в список клиентов
                clients.add(client)
                //Переходим к следующему клиенту
                cursor.moveToNext()
            }
            cursor.close()

            result = result + count.toString() + " записей"
            textView3?.setText(result)
            //Какие параметры клиента мы будем отображать в соответствующих
            //элементах из разметки adapter_item.xml
            //val from = arrayOf("lem")
            val from = arrayOf("lem", "lang", "pos", "desc")
            val to = intArrayOf(R.id.textView, R.id.textView2, R.id.textView4, R.id.textView5)
            //val to = intArrayOf(R.id.textView)
            //Создаем адаптер
            val adapter1 = SimpleAdapter(this, clients, R.layout.adapter_item, from, to)
            val listView = findViewById<View>(R.id.listView) as ListView
            listView.adapter = adapter1

        })

    }
}