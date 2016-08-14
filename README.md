# java-nio
`java nio api demo`

Java NIO 由以下几个核心部分组成：

    Channels
    Buffers
    Selectors
    
Channel，Buffer 和 Selector 构成了核心的API。其它组件，如Pipe和FileLock，只不过是与三个核心组件共同使用的工具类。

### Channel

通道类似流，但又有些不同：

    既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的(例如InputStream,OutPutStream)。
    通道可以异步地读写。
    通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。

JAVA NIO中的一些主要Channel的实现：

    FileChannel			从文件中读写数据
    DatagramChannel			通过UDP读写网络中的数据
    SocketChannel			通过TCP读写网络中的数据
    ServerSocketChannel		可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel

	这些通道涵盖了UDP 和 TCP 网络IO，以及文件IO。
	
Java NIO里关键的Buffer实现：

    ByteBuffer
    CharBuffer
    DoubleBuffer
    FloatBuffer
    IntBuffer
    LongBuffer
    ShortBuffer

	这些Buffer覆盖了你能通过IO发送的基本数据类型：byte, short, int, long, float, double 和 char。

	Java NIO 还有个 MappedByteBuffer，用于表示内存映射文件。
	
Selector

	Selector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。
	Selector用来轮询每个注册的Channel，一旦发现Channel有注册的事件发生，便获取事件然后进行处理。
	
	