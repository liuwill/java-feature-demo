# JAVA-FEATURE-DEMO

## 运行命令

### 编译模式

- `DEMETER_BUILD_MODE=agent make build` 编译Runner和Agent
- `make build` 编译Runner

### 打包

- `make package` 打包Runner，需要先编译

如果需要查看jar包内容，`jar vtf target/output/Runner.jar`

### 运行模式

`make run`使用run命令直接运行，需要先编译，支持的环境变量

- `DEMETER_AGENT_MODE=agent` 运行的时候使用Java Agent
- `DEMETER_RUN_MODE=jar` 运行package生成的Jar包
