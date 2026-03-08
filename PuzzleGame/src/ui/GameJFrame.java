package ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.Random;

//游戏主界面
public class GameJFrame extends JFrame implements KeyListener, ActionListener {
    //创建二维数组，保存图片的顺序
    int[][] data = new int[4][4];
    //记录空白方块的坐标
    int x, y;
    //记录当前展示图片的路径
    String path = "src\\images\\animal\\animal1\\";
    //定义二维数组存储正确的数据
    int[][] win = new int[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };
    //记录步数
    int step = 0;
    //创建子菜单项
    JMenuItem replayItem = new JMenuItem("重新开始");
    JMenuItem exitItem = new JMenuItem("退出游戏");
    JMenuItem aboutItem = new JMenuItem("博客网站");
    JMenuItem animalItem = new JMenuItem("动物");
    JMenuItem personItem = new JMenuItem("人物");
    JMenuItem sceneryItem = new JMenuItem("风景");

    //JFrame的子类也表示窗口
    public GameJFrame() {
        //初始化界面
        initJFrame();

        //初始化菜单
        initJMenuBar();

        //初始化数据
        initData();

        //初始化图片（根据打乱之后的结果加载图片）
        initImage();

        //让界面显示出来
        this.setVisible(true);
    }

    //初始化界面
    private void initJFrame() {
        //设置界面的宽高
        this.setSize(603, 680);
        //设置界面的标题
        this.setTitle("拼图单机版 v1.0");
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //取消默认的居中放置，只有取消了才会按照XY轴的形式添加组件
        this.setLayout(null);
        //给整个界面添加键盘监听事件
        this.addKeyListener(this);
    }

    //初始化菜单
    private void initJMenuBar() {
        //创建菜单
        JMenuBar jMenuBar = new JMenuBar();
        //创建菜单项
        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于我");
        JMenu changeJMenu = new JMenu("更换拼图");
        //添加子菜单项
        functionJMenu.add(changeJMenu);
        functionJMenu.add(replayItem);
        functionJMenu.add(exitItem);
        aboutJMenu.add(aboutItem);
        changeJMenu.add(animalItem);
        changeJMenu.add(personItem);
        changeJMenu.add(sceneryItem);
        //给条目绑定事件
        replayItem.addActionListener(this);
        exitItem.addActionListener(this);
        aboutItem.addActionListener(this);
        animalItem.addActionListener(this);
        personItem.addActionListener(this);
        sceneryItem.addActionListener(this);
        //添加菜单项
        jMenuBar.add(functionJMenu);
        jMenuBar.add(aboutJMenu);
        //给整个界面设置菜单
        this.setJMenuBar(jMenuBar);
    }

    //初始化数据
    private void initData() {
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Random r = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            int index = r.nextInt(tempArr.length);
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }
        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0) {
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    //初始化图片
    private void initImage() {
        //清除原本已经出现的图片
        this.getContentPane().removeAll();

        //判断胜利
        if (victor()) {
            JLabel winJLabel = new JLabel(new ImageIcon("src\\images\\win.jpg"));
            winJLabel.setBounds(203, 283, 197, 73);
            this.getContentPane().add(winJLabel);
        }

        //文字
        initText();

        //添加图片
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //获取当前要加载的图片的序号
                int num = data[i][j];
                //创建一个图片ImageIcon的库
                //创建一个JLabel对象（管理容器）
                JLabel jLabel = new JLabel(new ImageIcon(path + num + ".jpg"));
                //设置图片的位置
                jLabel.setBounds(105 * j + 83, 105 * i + 134, 105, 105);
                //给图片设置边框
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                //把管理容器添加到界面中
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景图片
        JLabel background = new JLabel(new ImageIcon("src\\images\\background.jpg"));
        background.setBounds(72, 122, 443, 443);
        this.getContentPane().add(background);

        //刷新界面
        this.getContentPane().repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //按下A不松显示整体预览图
    @Override
    public void keyPressed(KeyEvent e) {
        //判断是否胜利，如果胜利不能再移动
        if (victor()) {
            return;
        }
        int code = e.getKeyCode();
        if (code == 65) {
            //把界面中所有图片全部删除
            this.getContentPane().removeAll();
            //加载完整图片
            JLabel all = new JLabel(new ImageIcon(path + "all.jpg"));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);
            //加载背景图片
            ImageIcon bg = new ImageIcon("src\\images\\background.jpg");
            JLabel background = new JLabel(bg);
            background.setBounds(72, 122, 443, 443);
            this.getContentPane().add(background);
            //文字
            initText();
            //刷新界面
            this.getContentPane().repaint();
        }
    }

    //上下左右移动监听
    //松开A还原拼图
    //按下W一键通关
    @Override
    public void keyReleased(KeyEvent e) {
        //判断是否胜利，如果胜利不能再移动
        if (victor()) {
            return;
        }
        //上38 下40 左37 右39
        int code = e.getKeyCode();
        if (code == 37) {//左
            if (y != 3) {
                data[x][y] = data[x][y + 1];
                data[x][y + 1] = 0;
                y++;
                step++;
                initImage();
            }
        } else if (code == 38) {//上
            if (x != 3) {
                data[x][y] = data[x + 1][y];
                data[x + 1][y] = 0;
                x++;
                step++;
                initImage();
            }
        } else if (code == 39) {//右
            if (y != 0) {
                data[x][y] = data[x][y - 1];
                data[x][y - 1] = 0;
                y--;
                step++;
                initImage();
            }
        } else if (code == 40) {//下
            if (x != 0) {
                data[x][y] = data[x - 1][y];
                data[x - 1][y] = 0;
                x--;
                step++;
                initImage();
            }
        } else if (code == 65) {
            initImage();
        } else if (code == 87) {
            data = new int[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 0}
            };
            step = -1;
            x = 3;
            y = 3;
            initImage();
        }
    }

    //判断胜利
    public boolean victor() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != win[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    //菜单栏监听
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == replayItem) {
            replay();
        } else if (obj == exitItem) {
            //关闭虚拟机
            System.exit(0);
        } else if (obj == aboutItem) {
            //跳转博客
            try {
                URI uri = new URI("http://www.blogbyj.cn");
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(uri);
            } catch (Exception ex) {

            }
        } else if (obj == animalItem) {
            //随机挑选一张图片
            Random r = new Random();
            int index = r.nextInt(1, 5);
            path = "src\\images\\animal\\animal" + index + "\\";
            replay();
        } else if (obj == personItem) {
            //随机挑选一张图片
            Random r = new Random();
            int index = r.nextInt(1, 5);
            path = "src\\images\\person\\person" + index + "\\";
            replay();
        } else if (obj == sceneryItem) {
            //随机挑选一张图片
            Random r = new Random();
            int index = r.nextInt(1, 5);
            path = "src\\images\\scenery\\scenery" + index + "\\";
            replay();
        }
    }

    //重新开始
    public void replay() {
        //再次打乱数据
        initData();
        //步数归零
        step = 0;
        //重新加载图片
        initImage();
    }

    //文字
    public void initText() {
        //记录步数
        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setFont(new Font("微软雅黑", Font.BOLD, 17));
        stepCount.setBounds(80, 90, 100, 20);
        this.getContentPane().add(stepCount);

        //操作提示
        JLabel optJLabel1 = new JLabel("按 ↑ ↓ ← → 键移动拼图");
        optJLabel1.setFont(new Font("微软雅黑", Font.BOLD, 17));
        optJLabel1.setBounds(80, 20, 200, 20);
        this.getContentPane().add(optJLabel1);
        JLabel optJLabel2 = new JLabel("按住 A 键查看预览图，按 W 键一键通关");
        optJLabel2.setFont(new Font("微软雅黑", Font.BOLD, 17));
        optJLabel2.setBounds(80, 55, 350, 20);
        this.getContentPane().add(optJLabel2);
    }
}
