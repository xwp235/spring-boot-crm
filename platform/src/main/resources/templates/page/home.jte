<input type="file" id="fileInput">
<button onclick="uploadFileInChunks()">上传文件</button>
<button onclick="mergeFile()">合并文件</button>
<p id="status"></p>

<h1 class="text-3xl font-bold underline">
    Hello world!
</h1>

<div class="max-w-xl mx-auto bg-white p-6 rounded-2xl shadow-xl">
    <h2 class="text-2xl font-bold text-center mb-6">请选择预约日期</h2>

    <div id="calendar" class="grid grid-cols-7 gap-2 text-center text-sm">
        <!-- 日期会通过 JavaScript 自动填充 -->
    </div>
</div>

@raw
<script>
    // 模拟每一天的状态
    const dateStatusMap = {
        1: 'available',
        2: 'unavailable',
        3: 'unknown',
        4: 'available',
        5: 'available',
        6: 'unavailable',
        7: 'unknown',
        8: 'available',
        // 可以继续填充到 30（按月份天数来）
    };

    const calendarEl = document.getElementById('calendar');

    // 模拟 4 月的日历（1-30号）
    for (let day = 1; day <= 30; day++) {
        const status = dateStatusMap[day] || 'unknown';

        let symbol = '';
        let color = '';

        if (status === 'available') {
            symbol = '⭕';
            color = 'text-green-600 hover:bg-green-100';
        } else if (status === 'unavailable') {
            symbol = '❌';
            color = 'text-red-500 bg-red-50 cursor-not-allowed';
        } else {
            symbol = '➖';
            color = 'text-gray-500';
        }

        const dayEl = document.createElement('div');
        dayEl.className = `p-4 border rounded-xl ${color} transition cursor-pointer`;

        if (status === 'available') {
            dayEl.addEventListener('click', () => {
                alert(`你选择了：4月${day}日`);
            });
        }

        dayEl.innerHTML = `<div class="text-lg font-bold">${day}</div><div class="text-xl">${symbol}</div>`;

        calendarEl.appendChild(dayEl);
    }

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
@endraw
