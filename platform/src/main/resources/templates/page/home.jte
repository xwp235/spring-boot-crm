<input type="file" id="fileInput">
<button onclick="uploadFileInChunks()">上传文件</button>
<button onclick="mergeFile()">合并文件</button>
<p id="status"></p>

<h1 class="text-3xl font-bold underline bg-primary-1">
    Hello world!
</h1>

<div class="max-w-3xl mx-auto p-4">
    <!-- Step Indicator -->
    <div class="flex justify-between items-center mb-6">
        <div class="text-center">
            <div class="rounded-full bg-cyan-500 text-white w-8 h-8 flex items-center justify-center mx-auto">1</div>
            <div class="text-sm mt-2">初再診選択</div>
        </div>
        <div class="text-center">
            <div class="rounded-full bg-cyan-500 text-white w-8 h-8 flex items-center justify-center mx-auto">2</div>
            <div class="text-sm mt-2">メニュー選択</div>
        </div>
        <div class="text-center">
            <div class="rounded-full bg-cyan-500 text-white w-8 h-8 flex items-center justify-center mx-auto">3</div>
            <div class="text-sm mt-2">日時選択</div>
        </div>
        <div class="text-center">
            <div class="rounded-full bg-gray-300 text-gray-700 w-8 h-8 flex items-center justify-center mx-auto">4</div>
            <div class="text-sm mt-2">情報入力</div>
        </div>
        <div class="text-center">
            <div class="rounded-full bg-gray-300 text-gray-700 w-8 h-8 flex items-center justify-center mx-auto">5</div>
            <div class="text-sm mt-2">確認</div>
        </div>
    </div>

    <!-- Calendar Header -->
    <div class="bg-white p-4 rounded-lg shadow">
        <div class="flex justify-between items-center mb-4">
            <button id="prevMonth" class="text-sm text-cyan-600">&lt; 前月</button>
            <h2 id="calendarTitle" class="text-lg font-bold">2025年4月</h2>
            <button id="nextMonth" class="text-sm text-cyan-600">翌月 &gt;</button>
        </div>
        <div class="grid grid-cols-7 text-center text-sm text-gray-500 mb-2">
            <div>日</div>
            <div>月</div>
            <div>火</div>
            <div>水</div>
            <div>木</div>
            <div>金</div>
            <div>土</div>
        </div>
        <div id="calendarGrid" class="grid grid-cols-7 gap-2 text-center text-sm">
            <!-- Calendar dynamically rendered -->
        </div>
    </div>
</div>
<%--<div class="max-w-xl mx-auto bg-white p-6 rounded-2xl shadow-xl">--%>
<%--    <h2 class="text-2xl font-bold text-center mb-6">请选择预约日期</h2>--%>

<%--    <div id="calendar" class="grid grid-cols-7 gap-2 text-center text-sm">--%>
<%--        <!-- 日期会通过 JavaScript 自动填充 -->--%>
<%--    </div>--%>
<%--</div>--%>

@raw
<script>

    const calendarGrid = document.getElementById('calendarGrid');
    const calendarTitle = document.getElementById('calendarTitle');
    const prevMonthBtn = document.getElementById('prevMonth');
    const nextMonthBtn = document.getElementById('nextMonth');

    let currentDate = new Date();

    const renderCalendar = (date) => {
        calendarGrid.innerHTML = '';
        const year = date.getFullYear();
        const month = date.getMonth();
        calendarTitle.textContent = `${year}年${month + 1}月`;

        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        // Fill empty grid before first day
        for (let i = 0; i < firstDay; i++) {
            calendarGrid.innerHTML += '<div></div>';
        }

        for (let day = 1; day <= daysInMonth; day++) {
            const isAvailable = Math.random() > 0.5;
            const remaining = Math.floor(Math.random() * 13);

            let content = `
                <div class="border rounded-lg p-2 cursor-pointer ${isAvailable ? 'border-cyan-500 text-cyan-600' : 'text-gray-400'}">
                    <div class="text-base">${day}</div>
                    <div class="text-xs">${isAvailable ? `あと${remaining}件` : '-'}</div>
                </div>
            `;
            calendarGrid.innerHTML += content;
        }
    };

    renderCalendar(currentDate);

    prevMonthBtn.addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar(currentDate);
    });

    nextMonthBtn.addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar(currentDate);
    });

    // // 模拟每一天的状态
    // const dateStatusMap = {
    //     1: 'available',
    //     2: 'unavailable',
    //     3: 'unknown',
    //     4: 'available',
    //     5: 'available',
    //     6: 'unavailable',
    //     7: 'unknown',
    //     8: 'available',
    //     // 可以继续填充到 30（按月份天数来）
    // };
    //
    // const calendarEl = document.getElementById('calendar');
    //
    // // 模拟 4 月的日历（1-30号）
    // for (let day = 1; day <= 30; day++) {
    //     const status = dateStatusMap[day] || 'unknown';
    //
    //     let symbol = '';
    //     let color = '';
    //
    //     if (status === 'available') {
    //         symbol = '⭕';
    //         color = 'text-green-600 hover:bg-green-100';
    //     } else if (status === 'unavailable') {
    //         symbol = '❌';
    //         color = 'text-red-500 bg-red-50 cursor-not-allowed';
    //     } else {
    //         symbol = '➖';
    //         color = 'text-gray-500';
    //     }
    //
    //     const dayEl = document.createElement('div');
    //     dayEl.className = `p-4 border rounded-xl ${color} transition cursor-pointer`;
    //
    //     if (status === 'available') {
    //         dayEl.addEventListener('click', () => {
    //             alert(`你选择了：4月${day}日`);
    //         });
    //     }
    //
    //     dayEl.innerHTML = `<div class="text-lg font-bold">${day}</div><div class="text-xl">${symbol}</div>`;
    //
    //     calendarEl.appendChild(dayEl);
    // }

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
