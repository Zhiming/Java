package org.zzm.basicsort;

import java.util.Arrays;

public class Sort {

	public void selectSort(int[] a){
		//只需要到倒数第二个元素就可以i，最后一个自然会排好，i用于记录当前需要插入的位置
		for(int i = 0; i < a.length - 1; i++){
			//记录最小的元素的位置，这里minIndex需要从i开始，因为i位置的元素也要参加排序
			int minIndex = i;
			//i之前的都是已经排好序的，从i之后开始找最小的元素
			for(int j = i + 1; j < a.length; j++){
				//寻找最小值，并且记录其位置
				if(a[j] < a[minIndex]){
					minIndex = j;
				}
			}
			//交换i和minIndex两个位置的元素
			int temp = a[i];
			a[i] = a[minIndex];
			a[minIndex] = temp;
		}
	}
	
	public void bubbleSort(int[] a){
		//外层循环控制循环的总次数，对于一个长度为n的数组，需要比较n-1次
		for(int i = 1; i < a.length; i++){
			//用于标记是否出现了交换，如果一次循环以后，flag依然是false，说明已经排好序了，就可以直接结束循环
			boolean flag = false;
			//从后往前的i个元素已经比较过了，所以就不需要再比较
			for(int j = 0; j < a.length - i; j++){
				//如果后面的比前面大，需要交换位置
				//注意，交换位置以后，较大的元素依然会被j指向，因为j也自加了1
				if(a[j+1] < a[j]){
					int temp = a[j+1];
					a[j+1] = a[j];
					a[j] = temp;
					flag = true;
				}
			}
			if(!flag) break;
		}
	}
	
	public void mergeSort(int[] a) {
		if (a.length > 1) {
			int mid = a.length / 2;
			//注意复制数组的方法，已经有现成的API了
			int[] leftArr = Arrays.copyOfRange(a, 0, mid);
			int[] rightArr = Arrays.copyOfRange(a, mid, a.length);
			mergeSort(leftArr);
			mergeSort(rightArr);
			merge(a, leftArr, rightArr);
		}
	}

	/**
	 * 将两个已经拍好序的数组合并到一起
	 * @param original
	 * @param leftArr
	 * @param rightArr
	 */
	private void merge(int[] original, int[] leftArr, int[] rightArr) {
		int i = 0,  j = 0, k = 0;
		//挑选当前两个被合并到一起的数组的最小值，放入到返回的数组中去e
		while(i < leftArr.length && j < rightArr.length){
			if(leftArr[i] <= rightArr[j]){
				original[k] = leftArr[i];
				i++;
			}else{
				original[k] = rightArr[j];
				j++;
			}
			k++;
		}
		//不管哪个数组有剩余，将剩余的全部拷入返回数组中去
		while(i < leftArr.length){
			original[k] = leftArr[i];
			i++;
			k++;
		}
		while(j < rightArr.length){
			original[k] = rightArr[j];
			j++;
			k++;
		}
	}

	/**
	 * 插入排序
	 * @param a
	 */
	public void insertionSort(int[] a) {
		//wall之前是已经排好序的
		int wall = 0;
		for (int i = 1; i < a.length; i++) {
			//pendingValIndex表示待排序的元素，在下面的每次交换以后，仍然指向待排序的元素，知道该元素被插入到合适的位置
			//然后pendingValIndex将指向i现在所指的元素，即指向一个新的待排序的元素
			int pendingValIndex = i;
			//排序开始的时候，从wall开始向前推进，一直找到合适的位置
			for (int j = wall; j >= 0; j--) {
				int temp = a[pendingValIndex];
				//当待排序元素小于当前已经排好序的元素时，需要交换位置
				if (a[pendingValIndex] < a[j]) {
					a[pendingValIndex] = a[j];
					a[j] = temp;
					pendingValIndex--;
				}
			}
			wall = i;
		}
	}

	public void quickSort(int[] a, int start, int end) {
		if (start < end) {
			int pIndex = partition(a, start, end);
			quickSort(a, start, pIndex - 1);
			quickSort(a, pIndex + 1, end);
		}
	}

	/**
	 * 
	 * @param a
	 * @param start 从a的哪个位置开始排序
	 * @param end	到哪个位置结束
	 * @return
	 */
	private int partition(int[] a, int start, int end) {
		int pivot = a[end];
		//pIndex记录每次排序结束以后，pivot应该被插入到的位置，而在排序过程中，表示比pivot大，但是尚未交换的元素的位置
		int pIndex = start;
		//这里其实是有两个指针，一个是i，一个是pIndex,i用于记录当前待排序的元素的位置
		//pIndex表示pivot结束时候的插入位置，而在排序过程中，表示比pivot大，但是尚未交换的元素的位置
		//在排序的过程中，pIndex和i之间的元素都是比pivot大的，所以交换以后，pIndex自增了1，但是依然指向
		//一个比pivot大的元素，所以在循环结束时，才会出现pIndex左边的都是pivot小，右边都是比pivot大的情况
		
		//如果一个数列前面的元素都比pivot小，只是倒数第二个比pivot大，那么pIndex会停在倒数第二个，最后跳出循环，交换pivot和pIndex
		//或者如果一个数列已经拍好序了，那么在倒数第二个时，i和pIndex指向同一个元素（倒数第二个元素和自己交换），之后pIndex增加1，指向
		//最后一个元素，这时跳出循环，pIndex指向最后一个元素，然后pivot和其自己交换
		for (int i = start; i < end; i++) {
			//如果待排序元素比pivot小，就要用现在pIndex所指向的元素(排序过程中pIndex所指向的元素比pivot大)和其交换
			if (a[i] <= pivot) {
				int temp = a[i];
				a[i] = a[pIndex];
				a[pIndex] = temp;
				pIndex++;
			}
		}
		//遍历以后，交换pivot和pIndex所指向的元素，此时，pivot就在已经排序好的位置上了，然后递归排序其前后的数组
		int temp = a[pIndex];
		a[pIndex] = a[end];
		a[end] = temp;
		return pIndex;
	}

	public void show(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public static void main(String[] args) {
		Sort s = new Sort();
//		int[] a = { 2, 1, 3, 4, 8, 5, 7, 6, 10, 9 };
		int[] a = { 1, 2, 3, 4, 5, 6 };
		// s.insertionSort(a);
		 s.quickSort(a, 0, a.length - 1);
		s.mergeSort(a);
		s.selectSort(a);
//		s.bubbleSort(a);
		s.show(a);
	}
}
