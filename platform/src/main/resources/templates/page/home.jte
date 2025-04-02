<input type="file" id="fileInput">
<button onclick="uploadFileInChunks()">上传文件</button>
<button onclick="mergeFile()">合并文件</button>
<p id="status"></p>

<script>
    let fileName = '';

    /**
     * 拆分文件，每 2MB 进行拆分
     * @param {File} file - 需要上传的文件
     */
    const uploadFileInChunks = (file) => {
        const chunkSize = 1024 * 1024 * 2; // 2MB
        let start = 0;
        let chunkIndex = 0;

        while (start < file.size) {
            const chunk = file.slice(start, start + chunkSize);
            console.log(chunk);

            fileName = file.name;
            uploadChunk(chunk, chunkIndex, fileName);

            start += chunkSize;
            chunkIndex++;
        }
    };

    /**
     * 对每一个拆分的文件进行上传（小文件上传）
     * @param {Blob} chunk - 当前分片数据
     * @param {number} chunkIndex - 分片索引
     * @param {string} fileName - 文件名称
     */
    const uploadChunk = (chunk, chunkIndex, fileName) => {
        const formData = new FormData();
        formData.append('chunk', chunk);
        formData.append('chunkIndex', chunkIndex);
        formData.append('fileName', fileName);

        fetch('http://localhost:8080/upload-chunk', {
            method: 'POST',
            body: formData
        })
                .then(resp => resp.json()) // 解析 JSON 响应
                .then(data => console.log('Upload success:', data)) // 处理成功响应
                .catch(error => console.error('Upload failed:', error)); // 处理错误
    };

    /**
     * 合并文件
     */
    const mergeFile = () => {
        const formData = new FormData();
        formData.append('fileName', fileName);

        fetch('http://localhost:8080/merge-chunks', {
            method: 'POST',
            body: formData
        })
                .then(resp => resp.json()) // 解析 JSON 响应
                .then(data => console.log('Merge success:', data)) // 处理成功响应
                .catch(error => console.error('Merge failed:', error)); // 处理错误
    };
</script>
