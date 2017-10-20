package com.chb.mis.rabbitmq;

import android.app.Activity;
import android.graphics.Color;
import java.text.SimpleDateFormat;;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chb.mis.R;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.Date;

/**
 * Created by Administrator on 2017/10/19.
 */
public class RabbitMqActivity extends Activity {
    public static final int RABBITMQ_SEND_VIEW = 0;
    public static final int RABBITMQ_RECV_VIEW = 1;

    private final static String QUEUE_NAME = "queue"; //队列名称
    private static RabbitMqActivity instance = null;
    private TextView tx1, tx2;
    private Connection recvConnection;
    private Channel recvChannel;

    public static RabbitMqActivity getInstance() {
        synchronized (RabbitMqActivity.class) {
            if (instance == null) {
                synchronized (RabbitMqActivity.class) {
                    if (instance == null) {
                        instance = new RabbitMqActivity();
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
        setContentView(R.layout.rabbitmq);
        this.setTitle(R.string.rabbitmq_title);
        this.setTitleColor(Color.BLUE);

        final EditText ed1 = (EditText)findViewById(R.id.rabbitmq_et1);

        tx1 = (TextView) findViewById(R.id.tx1);
        tx1.setTextSize(18);
        tx1.setText("已发送消息");
        tx1.setTextColor(Color.YELLOW);

        tx2 = (TextView) findViewById(R.id.tx2);
        tx2.setTextSize(18);
        tx2.setText("收到的消息");
        tx2.setTextColor(Color.YELLOW);

        findViewById(R.id.rabbitmq_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String str = ed1.getText().toString();
                            sendDoit(str);
                        } catch (Exception e) {
                            tx1.setText(getCurDate() + e.toString());
                            System.out.println(e.toString());
                        }
                    }
                });
                thread.start();
            }
        });

        findViewById(R.id.rabbitmq_recv).setOnClickListener(new View.OnClickListener() {
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
                case RABBITMQ_SEND_VIEW:
                    //完成主界面更新,拿到数据
                    tx1.setText(data);
                    break;
                case RABBITMQ_RECV_VIEW:
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
//1.连接MabbitMQ所在主机ip或者主机名
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.219.21");
        factory.setRequestedHeartbeat(3);//连接心跳
        //factory.setHost("110.80.10.26");
        //factory.setPort(5672);
        //factory.setUsername("123");
        //factory.setPassword("123");
        //创建一个连接   创建一个频道
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //2.指定一个队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);


            //3.往队列中发出一条消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            //4.关闭频道和连接
            channel.close();
            connection.close();

            System.out.println("[CHB Send]" + message);
            sendMsg(RABBITMQ_SEND_VIEW,"[已发送]: " + message);
        } catch (Exception e) {
            sendMsg(RABBITMQ_SEND_VIEW, e.toString());
            System.out.println(e.toString());
        }
    }

    public void recvDoit() {
//1.打开连接和创建频道，与发送端一样
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.219.21");
        factory.setRequestedHeartbeat(3);//连接心跳
        //factory.setHost("110.80.10.26");
        //factory.setPort(5672);
        //factory.setUsername("123");
        //factory.setPassword("123");
        //创建一个连接   创建一个频道
        try {
            recvConnection = factory.newConnection();
            recvChannel = recvConnection.createChannel();
            //2.声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
            recvChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
            sendMsg(RABBITMQ_RECV_VIEW, getCurDate() + "Waiting for messages……");
            System.out.println("Waiting for messages……");

            //3.创建队列消费者
            QueueingConsumer consumer = new QueueingConsumer(recvChannel);
            recvChannel.basicConsume(QUEUE_NAME, true, consumer);//指定消费队列
            while (true) {
                //4.开启nextDelivery阻塞方法（内部实现其实是阻塞队列的take方法）
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                sendMsg(RABBITMQ_RECV_VIEW, getCurDate() + message);
                System.out.println("[CHB Received]" + message);
            }
        } catch (Exception e) {
            sendMsg(RABBITMQ_SEND_VIEW, e.toString());
            System.out.println(e.toString());
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
            if(recvChannel != null) {
                recvChannel.close();
                recvChannel = null;
            }
            if(recvConnection != null) {
                recvConnection.close();
                recvConnection = null;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}