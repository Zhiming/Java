package org.zzm.priorityqueue;

//���ڵ�������ӽڵ㶼���heap
public class PriorityQueue {
	
	private int[] array;
	
	public PriorityQueue(int[] a){
		array = new int[a.length + 1];
	}

	public int size() {
		return this.array.length - 1;
	}
	
	public void swim(int k){
		while(k > 1 && array[k] > array[k/2]){
			int temp = array[k/2];
			array[k] = temp;
			array[k] = array[k/2];
			array[k/2] = temp;
			k = k / 2;
		}
	}
	
	public void sink(int k){
		//�ж�k�Ƿ���Ҷ�ڵ�
		while(2 * k < this.array.length){
			int j = 2 * k;
			// N = array.length - 1��ע���һ��λ���ǲ��õ�
			//�ж��������Ƿ���ڣ����j�����һ��Ԫ�صĻ�����ôj��պõ���N���������j<N�Ļ�����ô������Ԫ�ش���
			if(j < this.array.length - 1){
				//ȷ�������ӽڵ��ĸ���
				if(this.array[j] < this.array[j + 1]){
					j++;
				}
			}
			
			//�жϸ��ڵ��Ƿ�Ƚϴ���ӽڵ��
			if(array[k] > array[j]) break;
			
			//������Ԫ�غ�����Ԫ��
			int temp = array[k];
			array[k] = array[j];
			array[j] = temp;
		}
	}
	
}
