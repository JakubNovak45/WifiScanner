import mysql.connector
from flask import Flask
from flask import request, render_template, redirect, url_for

app = Flask(__name__)

def createDB(cursor):
       cursor.execute("DROP DATABASE WifiList")
       cursor.execute("""CREATE TABLE WifiList (id INT AUTO_INCREMENT PRIMARY KEY,
               SSID VARCHAR(255) NOT NULL,
               BSSID VARCHAR(255) NOT NULL,
               security VARCHAR(255),
               center_freq1 INT,
               center_freq2 INT,
               frequency INT,
               level VARCHAR(255),
               channelWidth VARCHAR(255),
               timeStamp DATETIME NOT NULL)""")

def getDB():
    db = mysql.connector.connect(
       host = "srv1.trnila.eu",
       user = "kuba",
       passwd = "kubuvserver",
       database = "wifi"
    )
    cursor = db.cursor()
    sql = "SET time_zone = 'Europe/Prague'"
    cursor.execute(sql)
    return db


@app.route("/json", methods=['GET', 'POST'])
def json():
    db = getDB()
    cursor = db.cursor()
    app.logger.debug("JSON recived..")
    app.logger.debug(request.get_json())

    if request.json:
        data = request.json
        sql = "INSERT INTO WifiList" + \
        "(SSID, BSSID, security, center_freq1, center_freq2, frequency, level, channelWidth, timeStamp) " + \
	"VALUES (%s, %s, %s, %s, %s, %s, %s, %s, NOW())"
        val = (data.get("ssid"),
               data.get("bssid"),
               data.get("capabilities"),
               data.get("centerfreq0"),
               data.get("centerfreq1"),
               data.get("frequency"),
               data.get("level"),
               data.get("channelWidth"))
        i = cursor.execute(sql, val)
	db.commit()
	print(i)
	db.close()
        return "recived"
    else:
	db.close()
        return "not-recived"

@app.route('/')
def listWifi():
	db = getDB()
    	cursor = db.cursor(dictionary=True)

	sql = "SELECT * FROM WifiList"
	cursor.execute(sql)
	wifi = cursor.fetchall()
	return render_template('./index.html', wifi = wifi)

@app.route('/dataTablePage', methods=['POST'])
def deleteDataPage():
        db = getDB()
        cursor = db.cursor(dictionary=True)
	sql = "DELETE FROM WifiList"
	cursor.execute(sql)
	db.commit()
	return redirect(url_for('listWifi'))

app.run(host='::', debug=True)


