package org.zzm.basicsort;

import java.util.Arrays;

public class Sort {

	public void selectSort(int[] a){
		//ֻ��Ҫ�������ڶ���Ԫ�ؾͿ���i�����һ����Ȼ���źã�i���ڼ�¼��ǰ��Ҫ�����λ��
		for(int i = 0; i < a.length - 1; i++){
			//��¼��С��Ԫ�ص�λ�ã�����minIndex��Ҫ��i��ʼ����Ϊiλ�õ�Ԫ��ҲҪ�μ�����
			int minIndex = i;
			//i֮ǰ�Ķ����Ѿ��ź���ģ���i֮��ʼ����С��Ԫ��
			for(int j = i + 1; j < a.length; j++){
				//Ѱ����Сֵ�����Ҽ�¼��λ��
				if(a[j] < a[minIndex]){
					minIndex = j;
				}
			}
			//����i��minIndex����λ�õ�Ԫ��
			int temp = a[i];
			a[i] = a[minIndex];
			a[minIndex] = temp;
		}
	}
	
	public void bubbleSort(int[] a){
		//���ѭ������ѭ�����ܴ���������һ������Ϊn�����飬��Ҫ�Ƚ�n-1��
		for(int i = 1; i < a.length; i++){
			//���ڱ���Ƿ�����˽��������һ��ѭ���Ժ�flag��Ȼ��false��˵���Ѿ��ź����ˣ��Ϳ���ֱ�ӽ���ѭ��
			boolean flag = false;
			//�Ӻ���ǰ��i��Ԫ���Ѿ��ȽϹ��ˣ����ԾͲ���Ҫ�ٱȽ�
			for(int j = 0; j < a.length - i; j++){
				//�������ı�ǰ�����Ҫ����λ��
				//ע�⣬����λ���Ժ󣬽ϴ��Ԫ����Ȼ�ᱻjָ����ΪjҲ�Լ���1
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
			//ע�⸴������ķ������Ѿ����ֳɵ�API��
			int[] leftArr = Arrays.copyOfRange(a, 0, mid);
			int[] rightArr = Arrays.copyOfRange(a, mid, a.length);
			mergeSort(leftArr);
			mergeSort(rightArr);
			merge(a, leftArr, rightArr);
		}
	}

	/**
	 * �������Ѿ��ĺ��������ϲ���һ��
	 * @param original
	 * @param leftArr
	 * @param rightArr
	 */
	private void merge(int[] original, int[] leftArr, int[] rightArr) {
		int i = 0,  j = 0, k = 0;
		//��ѡ��ǰ�������ϲ���һ����������Сֵ�����뵽���ص�������ȥe
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
		//�����ĸ�������ʣ�࣬��ʣ���ȫ�����뷵��������ȥ
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
	 * ��������
	 * @param a
	 */
	public void insertionSort(int[] a) {
		//wall֮ǰ���Ѿ��ź����
		int wall = 0;
		for (int i = 1; i < a.length; i++) {
			//pendingValIndex��ʾ�������Ԫ�أ��������ÿ�ν����Ժ���Ȼָ��������Ԫ�أ�֪����Ԫ�ر����뵽���ʵ�λ��
			//Ȼ��pendingValIndex��ָ��i������ָ��Ԫ�أ���ָ��һ���µĴ������Ԫ��
			int pendingValIndex = i;
			//����ʼ��ʱ�򣬴�wall��ʼ��ǰ�ƽ���һֱ�ҵ����ʵ�λ��
			for (int j = wall; j >= 0; j--) {
				int temp = a[pendingValIndex];
				//��������Ԫ��С�ڵ�ǰ�Ѿ��ź����Ԫ��ʱ����Ҫ����λ��
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
	 * @param start ��a���ĸ�λ�ÿ�ʼ����
	 * @param end	���ĸ�λ�ý���
	 * @return
	 */
	private int partition(int[] a, int start, int end) {
		int pivot = a[end];
		//pIndex��¼ÿ����������Ժ�pivotӦ�ñ����뵽��λ�ã�������������У���ʾ��pivot�󣬵�����δ������Ԫ�ص�λ��
		int pIndex = start;
		//������ʵ��������ָ�룬һ����i��һ����pIndex,i���ڼ�¼��ǰ�������Ԫ�ص�λ��
		//pIndex��ʾpivot����ʱ��Ĳ���λ�ã�������������У���ʾ��pivot�󣬵�����δ������Ԫ�ص�λ��
		//������Ĺ����У�pIndex��i֮���Ԫ�ض��Ǳ�pivot��ģ����Խ����Ժ�pIndex������1��������Ȼָ��
		//һ����pivot���Ԫ�أ�������ѭ������ʱ���Ż����pIndex��ߵĶ���pivotС���ұ߶��Ǳ�pivot������
		
		//���һ������ǰ���Ԫ�ض���pivotС��ֻ�ǵ����ڶ�����pivot����ôpIndex��ͣ�ڵ����ڶ������������ѭ��������pivot��pIndex
		//�������һ�������Ѿ��ĺ����ˣ���ô�ڵ����ڶ���ʱ��i��pIndexָ��ͬһ��Ԫ�أ������ڶ���Ԫ�غ��Լ���������֮��pIndex����1��ָ��
		//���һ��Ԫ�أ���ʱ����ѭ����pIndexָ�����һ��Ԫ�أ�Ȼ��pivot�����Լ�����
		for (int i = start; i < end; i++) {
			//���������Ԫ�ر�pivotС����Ҫ������pIndex��ָ���Ԫ��(���������pIndex��ָ���Ԫ�ر�pivot��)���佻��
			if (a[i] <= pivot) {
				int temp = a[i];
				a[i] = a[pIndex];
				a[pIndex] = temp;
				pIndex++;
			}
		}
		//�����Ժ󣬽���pivot��pIndex��ָ���Ԫ�أ���ʱ��pivot�����Ѿ�����õ�λ�����ˣ�Ȼ��ݹ�������ǰ�������
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
