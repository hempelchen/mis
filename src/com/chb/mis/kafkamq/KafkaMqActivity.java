package com.chb.mis.kafkamq;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chb.mis.R;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Administrator on 2017/10/25.
 */
public class KafkaMqActivity extends Activity {

    public static final int RKAFKAMQ_SEND_VIEW = 0;
    public static final int RKAFKAMQ_RECV_VIEW = 1;

    private static KafkaMqActivity instance = null;
    private TextView tx1, tx2;
    
    public static KafkaMqActivity getInstance() {
        synchronized (KafkaMqActivity.class) {
            if (instance == null) {
                synchronized (KafkaMqActivity.class) {
                    if (instance == null) {
                        instance = new KafkaMqActivity();
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kafkamq);
        this.setTitle(R.string.kafkamq_title);
        this.setTitleColor(Color.BLUE);
//        final EditText ed1 = (EditText)findViewById(R.id.kafkamq_et1);

//        tx1 = (TextView) findViewById(R.id.tx1);
//        tx1.setTextSize(18);
//        tx1.setText("已发送消息");
//        tx1.setTextColor(Color.YELLOW);

        tx2 = (TextView) findViewById(R.id.tx2);
        tx2.setTextSize(18);
        tx2.setText("暂时没有Android Client Jar，无法接收消息（源代码中的jar包是JVM编译）");
        tx2.setTextColor(Color.YELLOW);

//        findViewById(R.id.kafkamq_send).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String str = ed1.getText().toString();
//                            sendDoit(str);
//                        } catch (Exception e) {
//                            tx1.setText(getCurDate() + e.toString());
//                            System.out.println(e.toString());
//                        }
//                    }
//                });
//                thread.start();
//            }
//        });

        findViewById(R.id.kafkamq_recv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            recvDoit();
                        } catch (Exception e) {
                            tx2.setText(getCurDate() + e.toString());
                            System.out.println(e.toString());
                        }
                    }
                });
                thread.start();
            }
        });
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = (String)msg.obj;
            switch (msg.what) {
                case RKAFKAMQ_SEND_VIEW:
                    //完成主界面更新,拿到数据
                    tx1.setText(data);
                    break;
                case RKAFKAMQ_RECV_VIEW:
                    //完成主界面更新,拿到数据
                    tx2.setText(data);
                    break;
                default:
                    break;
            }
        }

    };

    public void sendMsg(int what, String message){
        //需要数据传递，用下面方法；
        Message msg =new Message();
        msg.what = what;
        msg.obj = message==null?"NULL":message;//可以是基本类型，可以是对象，可以是List、map等；
        mHandler.sendMessage(msg);
    }

    public void sendDoit(String message) {
    }

    public void recvDoit1(){
//        ConsumerGroupExample.main("102.168.219.20:2181", "group-1", "test", 1);
    }

    public void recvDoit() {
        Properties props = new Properties();

        props.put("bootstrap.servers", "192.168.219.20:9092");
        System.out.println("this is the group part test 1");
        sendMsg(RKAFKAMQ_SEND_VIEW, "[CHB] 1");
        //消费者的组id
        props.put("group.id", "test");//这里是GroupA或者GroupB

        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");

        //从poll(拉)的回话处理时长
        props.put("session.timeout.ms", "30000");
        //poll的数量限制
        //props.put("max.poll.records", "100");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        sendMsg(RKAFKAMQ_SEND_VIEW, "[CHB] 2");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        sendMsg(RKAFKAMQ_SEND_VIEW, "[CHB] 3");
        //订阅主题列表topic
        consumer.subscribe(Arrays.asList("test"));


        while (true) {
            sendMsg(RKAFKAMQ_SEND_VIEW, "[CHB] 4");
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                //　正常这里应该使用线程池处理，不应该在这里处理
                sendMsg(RKAFKAMQ_SEND_VIEW, "[CHB] 5");
                String msg = String.format("offset = %d, key = %s, value = %s", record.offset());
                sendMsg(RKAFKAMQ_SEND_VIEW, msg);
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value() + "\n");
            }
        }
    }

    public String getCurDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SSS");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return "["+str+"]: ";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}