<input type="file" id="fileInput">
<button onclick="uploadFileInChunks()">上传文件</button>
<button onclick="mergeFile()">合并文件</button>
<p id="status"></p>

<script>
    let filename = '';

    /**
     * 拆分文件，每 2MB 进行拆分
     * @param {File} file - 需要上传的文件
     */
    const uploadFileInChunks = () => {
        var file = fileInput.files[0]
        console.log(file)
        const chunkSize = 1024 * 1024 * 2; // 2MB
        let start = 0;
        let chunkIndex = 0;

        while (start < file.size) {
            const chunk = file.slice(start, start + chunkSize);

            filename = file.name;
            uploadChunk(chunk, chunkIndex, filename);

            start += chunkSize;
            chunkIndex++;
        }
    };

    /**
     * 对每一个拆分的文件进行上传（小文件上传）
     * @param {Blob} chunk - 当前分片数据
     * @param {number} chunkIndex - 分片索引
     * @param {string} filename - 文件名称
     */
    const uploadChunk = (chunk, chunkIndex, filename) => {
        const formData = new FormData();
        formData.append('chunkFile', chunk);
        formData.append('chunkIndex', chunkIndex);
        formData.append('filename', filename);

        console.log(formData)
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
        formData.append('filename', filename);

        fetch('http://localhost:8080/merge-chunks', {
            method: 'POST',
            body: formData
        })
                .then(resp => resp.json()) // 解析 JSON 响应
                .then(data => console.log('Merge success:', data)) // 处理成功响应
                .catch(error => console.error('Merge failed:', error)); // 处理错误
    };
</script>
